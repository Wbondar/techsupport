/**
 * @Author Vladyslav Bondarenko
 * @Date   2015-08-29
 */

CREATE TABLE member
(
      id            INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
    , name          VARCHAR(100)      NOT NULL
    , password_hash TEXT              NOT NULL 
    , created_at    TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    , UNIQUE KEY (name)
)
;
 
CREATE TABLE message
(
      id        INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT
    , parent_id INTEGER UNSIGNED      NULL
    , author_id INTEGER UNSIGNED NOT NULL
    , content   TEXT              NOT NULL
    , posted_at TIMESTAMP         NOT NULL
    /**
     * It would be nicer to use a composite key for primary key of "message" table
     * but for the sake of simplisity and flexebility
     * surrogate key is used instead.
     */
    , PRIMARY KEY (id)
    , FOREIGN KEY (parent_id) REFERENCES message (id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
    , FOREIGN KEY (author_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , INDEX (author_id, id)
)  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC
;
 
CREATE TABLE issue 
(
      id             INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT
    , issuer_id      INTEGER UNSIGNED NOT NULL
    , title          VARCHAR(100)      NOT NULL
    , description_id INTEGER UNSIGNED  NOT NULL
    , created_at     TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    /**
     * Issue composite foreign key is needed in order to ensure 
     * that the author of a first comment on an issue (desription of an issue) is
     * the same as an author of an issue itself. 
     */ 
    , FOREIGN KEY (issuer_id, description_id) REFERENCES message (author_id, id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , UNIQUE KEY (title)
)  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC
;

CREATE TABLE comment 
(
      issue_id   INTEGER UNSIGNED NOT NULL 
    , message_id INTEGER UNSIGNED NOT NULL
    , PRIMARY KEY (issue_id, message_id)
    , FOREIGN KEY (issue_id) REFERENCES issue (id)
        ON DELETE CASCADE 
        ON UPDATE RESTRICT 
    , FOREIGN KEY (message_id) REFERENCES message (id)
        ON DELETE RESTRICT 
        ON UPDATE RESTRICT
)  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC
;

CREATE TABLE tag
(
      id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
    , title      VARCHAR(100)      NOT NULL
    , created_at TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    , UNIQUE KEY (title)
)  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci ROW_FORMAT=DYNAMIC
;

CREATE TABLE tag_usage
(
      issue_id INTEGER UNSIGNED  NOT NULL
    , tag_id   INTEGER UNSIGNED NOT NULL
    , PRIMARY KEY (issue_id, tag_id)
    , FOREIGN KEY (issue_id) REFERENCES issue (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
    , FOREIGN KEY (tag_id)   REFERENCES tag (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
)
;

/**
 * Fact of (un)assignation of a tag to an issue
 * is stored in multiple tables.
 * "tag_(un)assignation" tables store historical data
 * and "tag_usage" stores only actual.
 * It is done that way in order to simplify main schema.
 */ 

CREATE TABLE tag_assignation
(
      id          INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT
    , assigner_id INTEGER UNSIGNED NOT NULL
    , issue_id    INTEGER UNSIGNED  NOT NULL
    , tag_id      INTEGER UNSIGNED NOT NULL
    , assigned_at TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    , FOREIGN KEY (assigner_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (issue_id)    REFERENCES issue (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (tag_id)      REFERENCES tag (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , UNIQUE KEY (assigner_id, issue_id, tag_id, assigned_at)
    , INDEX (issue_id, tag_id)
)
;

CREATE TABLE tag_unassignation
(
      id            INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
    , unassigner_id INTEGER UNSIGNED NOT NULL
    , issue_id      INTEGER UNSIGNED NOT NULL
    , tag_id        INTEGER UNSIGNED NOT NULL
    , unassigned_at TIMESTAMP        NOT NULL
    , PRIMARY KEY (id)
    /**
     * Composed foreign key ensures that one cant' unassign tag, 
     * that was never assigned.
     */
    , FOREIGN KEY (issue_id, tag_id) REFERENCES tag_assignation (issue_id, tag_id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , UNIQUE KEY (unassigner_id, issue_id, tag_id, unassigned_at)
)
;

CREATE TABLE action 
(
      id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
    , label      VARCHAR(100)     NOT NULL
    , created_at TIMESTAMP        NOT NULL 
    , PRIMARY KEY (id)
    , UNIQUE KEY (label)
)
;

CREATE TABLE permission
(
      id         INTEGER UNSIGNED NOT NULL AUTO_INCREMENT
    , grantor_id INTEGER UNSIGNED NOT NULL
    , grantee_id INTEGER UNSIGNED NOT NULL
    , action_id  INTEGER UNSIGNED NOT NULL
    , granted_at TIMESTAMP        NOT NULL
    , PRIMARY KEY (id)
    , FOREIGN KEY (grantor_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (grantee_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (action_id) REFERENCES action (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    /**
     * The following unique key is applied in order to avoid
     * granting permission to perform the same action
     * multiple times by different grantors.
     */
    , UNIQUE KEY (grantee_id, action_id, granted_at)
    /**
     * One cannot grant the same action to the same grantee at the same time.
     */
    , UNIQUE KEY (grantor_id, grantee_id, action_id, granted_at)
)
;

CREATE TABLE permission_revocation
(
      permission_id INTEGER UNSIGNED NOT NULL
    , revoker_id    INTEGER UNSIGNED NOT NULL
    , revoked_at    TIMESTAMP        NOT NULL
    /**
     * The same permission can not be revoked multiple times.
     * In order to renew permission, one has to grant it all other again.
     */
    , PRIMARY KEY (permission_id)
    , FOREIGN KEY (permission_id) REFERENCES permission (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (revoker_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
)
;

DELIMITER ENDROUTINE
CREATE PROCEDURE issue_create
(
      IN  arg_issuer_id   INTEGER UNSIGNED 
    , IN  arg_title       VARCHAR (100)
    , IN  arg_description TEXT
    , OUT arg_issue_id    INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO message (author_id, content, posted_at) 
    VALUES (arg_issuer_id, arg_description, NOW( ))
    ; 
    INSERT INTO issue (title, issuer_id, description_id) 
    VALUES (arg_title, arg_issuer_id, LAST_INSERT_ID( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_issue_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE issue_update_tag_assign
(
      IN arg_assigner_id INTEGER UNSIGNED
    , IN arg_issue_id    INTEGER UNSIGNED 
    , IN arg_tag_id      INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO tag_usage (issue_id, tag_id) 
    VALUES(arg_issue_id, arg_tag_id)
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE issue_update_tag_unassign
(
      IN arg_unassigner_id INTEGER UNSIGNED
    , IN arg_issue_id      INTEGER UNSIGNED 
    , IN arg_tag_id        INTEGER UNSIGNED 
)
BEGIN
    DELETE FROM tag_usage 
    WHERE CONCAT (issue_id, tag_id) = CONCAT (arg_issue_id, arg_tag_id)
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE member_create
(
      IN  arg_name          VARCHAR (100)
    , IN  arg_password_hash TEXT
    , OUT arg_member_id     INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO member (name, password_hash, created_at)
    VALUES (LOWER(arg_name), arg_password_hash, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_member_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE tag_create
(
      IN  arg_title  VARCHAR (100)
    , OUT arg_tag_id INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO tag (title, created_at)
    VALUES (arg_title, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_tag_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE message_create
(
      IN  arg_parent_id   INTEGER UNSIGNED 
    , IN  arg_author_id   INTEGER UNSIGNED 
    , IN  arg_description TEXT
    , OUT arg_message_id  INTEGER UNSIGNED 
    , OUT arg_posted_at   TIMESTAMP
)
BEGIN
    INSERT INTO message (parent_id, author_id, content, posted_at)
    VALUES (arg_parent_id, arg_author_id, arg_description, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_message_id 
    ;
    SELECT NOW( ) INTO arg_posted_at
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE response_create
(
      IN  arg_parent_id   INTEGER UNSIGNED 
    , IN  arg_author_id   INTEGER UNSIGNED 
    , IN  arg_description TEXT
    , OUT arg_message_id  INTEGER UNSIGNED 
    , OUT arg_posted_at   TIMESTAMP
)
BEGIN
    CALL message_create (arg_parent_id, arg_author_id, arg_description, arg_message_id, arg_posted_at)
    ;
    SELECT issue_id INTO @var_issue_id 
    FROM view_comment 
    WHERE view_comment.id = arg_parent_id 
    ;
    INSERT INTO comment (issue_id, message_id)
    VALUES (@var_issue_id, arg_message_id)
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE comment_create
(
      IN  arg_issue_id    INTEGER UNSIGNED 
    , IN  arg_author_id   INTEGER UNSIGNED 
    , IN  arg_description TEXT
    , OUT arg_message_id  INTEGER UNSIGNED 
    , OUT arg_posted_at   TIMESTAMP
)
BEGIN
    SELECT issue.description_id INTO @var_parent_id
    FROM issue 
    WHERE issue.id = arg_issue_id
    ;
    CALL message_create (@var_parent_id, arg_author_id, arg_description, arg_message_id, arg_posted_at)
    ;
    INSERT INTO comment (issue_id, message_id)
    VALUES (arg_issue_id, arg_message_id)
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE action_create
(
      IN  arg_label  VARCHAR (100)
    , OUT arg_action_id INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO action (label, created_at)
    VALUES (arg_label, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_action_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE member_update_permission_grant
(
      IN  arg_grantor_id    INTEGER UNSIGNED
    , IN  arg_grantee_id    INTEGER UNSIGNED 
    , IN  arg_action_id     INTEGER UNSIGNED 
    , OUT arg_permission_id INTEGER UNSIGNED
    , OUT arg_granted_at    TIMESTAMP
    , OUT arg_valid         BOOLEAN
)
BEGIN
    INSERT INTO permission (grantor_id, grantee_id, action_id, granted_at) 
    VALUES (arg_grantor_id, arg_grantee_id, arg_action_id, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ), NOW( ), TRUE INTO arg_permission_id, arg_granted_at, arg_valid
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE member_update_permission_revoke
(
      IN  arg_revoker_id    INTEGER UNSIGNED
    , IN  arg_permission_id INTEGER UNSIGNED 
    , OUT arg_revoked_at    TIMESTAMP 
)
BEGIN
    INSERT INTO permission_revocation (revoker_id, permission_id, revoked_at)
    VALUES (arg_revoker_id, arg_permission_id, NOW( ))
    ;
    SELECT NOW( ) INTO arg_revoked_at
    ; 
END 
ENDROUTINE
DELIMITER ;

CREATE VIEW view_issue AS 
SELECT issue.id, issue.created_at, issue.issuer_id, issue.title, message.content AS message  
FROM issue JOIN message ON issue.description_id = message.id 
;

CREATE VIEW view_member AS 
SELECT * FROM member 
;

CREATE VIEW view_tag AS 
SELECT * FROM tag 
;

CREATE VIEW view_tag_usage AS
SELECT tag_usage.issue_id, view_tag.*
FROM tag_usage JOIN view_tag ON tag_usage.tag_id = view_tag.id 
;

CREATE VIEW view_comment AS 
SELECT comment.issue_id, message.* 
FROM comment JOIN message ON comment.message_id = message.id
;

CREATE VIEW view_action AS 
SELECT *
FROM action
;

CREATE VIEW view_permission AS 
SELECT 
      p.id AS id 
    , p.grantor_id
    , p.grantee_id 
    , p.action_id 
    , p.granted_at AS since
    , r.revoked_at AS until
    , r.revoker_id 
    , r.revoker_id IS NULL AS valid
FROM permission AS p 
LEFT JOIN permission_revocation AS r ON p.id = r.permission_id 
WHERE granted_at >= (SELECT MAX(granted_at) FROM permission AS c WHERE CONCAT(p.grantee_id, p.action_id) = CONCAT(c.grantee_id, c.action_id))
ORDER BY grantee_id, action_id, since, until DESC;
;

SELECT "Database successfully deployed to the server."
;
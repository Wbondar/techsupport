/**
 * @Author Vladyslav Bondarenko
 * @Date   2015-08-20
 */

CREATE TABLE member
(
      id            SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT
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
    , author_id SMALLINT UNSIGNED NOT NULL
    , content   TEXT              NOT NULL
    , posted_at TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    , FOREIGN KEY (parent_id) REFERENCES message (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (author_id) REFERENCES member (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , INDEX (author_id, id)
)
;
 
CREATE TABLE issue 
(
      id             INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT
    , author_id      SMALLINT UNSIGNED NOT NULL
    , title          VARCHAR(100)      NOT NULL
    , description_id INTEGER UNSIGNED  NOT NULL
    , created_at     TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    /**
     * Issue composite foreign key is needed in order to ensure 
     * that the author of a first comment on an issue (desription of an issue) is
     * the same as an author of an issue itself. 
     */ 
    , FOREIGN KEY (author_id, description_id) REFERENCES message (author_id, id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , UNIQUE KEY (title)
)
;

CREATE TABLE tag
(
      id         SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT
    , title      VARCHAR(100)      NOT NULL
    , created_at TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    , UNIQUE KEY (title)
)
;

CREATE TABLE tag_usage
(
      issue_id INTEGER UNSIGNED  NOT NULL
    , tag_id   SMALLINT UNSIGNED NOT NULL
    , PRIMARY KEY (issue_id, tag_id)
    , FOREIGN KEY (issue_id) REFERENCES issue (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , FOREIGN KEY (tag_id)   REFERENCES tag (id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
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
    , assigner_id SMALLINT UNSIGNED NOT NULL
    , issue_id    INTEGER UNSIGNED  NOT NULL
    , tag_id      SMALLINT UNSIGNED NOT NULL
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
      id            INTEGER UNSIGNED  NOT NULL AUTO_INCREMENT
    , unassigner_id SMALLINT UNSIGNED NOT NULL
    , issue_id      INTEGER UNSIGNED  NOT NULL
    , tag_id        SMALLINT UNSIGNED NOT NULL
    , unassigned_at TIMESTAMP         NOT NULL
    , PRIMARY KEY (id)
    /*
     * Composed foreign key ensures that one cant' unassign tag, 
     * that was never assigned.
     */
    , FOREIGN KEY (issue_id, tag_id) REFERENCES tag_assignation (issue_id, tag_id)
        ON DELETE RESTRICT
        ON UPDATE RESTRICT
    , UNIQUE KEY (unassigner_id, issue_id, tag_id, unassigned_at)
)
;

DELIMITER ENDROUTINE
CREATE PROCEDURE issue_create
(
      IN  arg_author_id   SMALLINT UNSIGNED 
    , IN  arg_title       VARCHAR (100)
    , IN  arg_description TEXT
    , OUT arg_issue_id    INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO message (author_id, content, posted_at) 
    VALUES (arg_author_id, arg_description, NOW( ))
    ; 
    INSERT INTO issue (title, author_id, description_id) 
    VALUES (arg_title, arg_author_id, LAST_INSERT_ID( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_issue_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE issue_update_tag_assign
(
      IN arg_assigner_id SMALLINT UNSIGNED
    , IN arg_issue_id    INTEGER UNSIGNED 
    , IN arg_tag_id      SMALLINT UNSIGNED 
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
      IN arg_unassigner_id SMALLINT UNSIGNED
    , IN arg_issue_id      INTEGER UNSIGNED 
    , IN arg_tag_id        SMALLINT UNSIGNED 
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
    , OUT arg_member_id     SMALLINT UNSIGNED 
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
    , OUT arg_tag_id SMALLINT UNSIGNED 
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
CREATE PROCEDURE response_create
(
      IN  arg_parent_id   INTEGER UNSIGNED 
    , IN  arg_author_id   SMALLINT UNSIGNED 
    , IN  arg_description TEXT
    , OUT arg_message_id  INTEGER UNSIGNED 
)
BEGIN
    INSERT INTO message (parent_id, author_id, content, posted_at)
    VALUES (arg_parent_id, arg_author_id, arg_description, NOW( ))
    ;
    SELECT LAST_INSERT_ID( ) INTO arg_message_id 
    ;
END 
ENDROUTINE
DELIMITER ;

DELIMITER ENDROUTINE
CREATE PROCEDURE comment_create
(
      IN  arg_issue_id    INTEGER UNSIGNED 
    , IN  arg_author_id   SMALLINT UNSIGNED 
    , IN  arg_description TEXT
    , OUT arg_message_id  INTEGER UNSIGNED 
)
BEGIN
    SELECT issue.description_id INTO @var_parent_id
    FROM issue 
    WHERE issue.id = arg_issue_id
    ;
    CALL response_create (@var_parent_id, arg_author_id, arg_description)
    ;
END 
ENDROUTINE
DELIMITER ;

CREATE VIEW view_issue AS 
SELECT * FROM issue 
;

CREATE VIEW view_member AS 
SELECT * FROM member 
;

CREATE VIEW view_tag AS 
SELECT * FROM tag 
;

CREATE VIEW view_comment AS 
SELECT * FROM message 
;

SELECT "Database successfully deployed to the server."
;
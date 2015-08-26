<!DOCTYPE html>
<html>
<head>
</head>
<body>
	<aside>
		<section>
			<h2>Sign up.</h2>
			<form action='/member/create' method='POST' >
			    <input type='text' name='username' />
			    <input type='password' name='password' />
			    <input type='submit' />
			</form>
		</section>
		<section>
			<h2>Log in.</h2>
			<form action='/session/create' method='POST' >
			    <input type='text' name='username' />
			    <input type='password' name='password' />
			    <input type='submit' />
			</form>
		</section>
		<section>
			<h2>Exit.</h2>
			<form action='/session/destroy' method='POST' >
			    <input type='submit' />
			</form>
		</section>
		<section>
			<h2>New issue.</h2>
			<form action='/issue/create' method='POST' >
			    <input type='text' name='title' />
			    <textarea name='message' cols='30' rows='6'></textarea>
			    <input type='submit' />
			</form>
		</section>
	</aside>
	<main>
		<h1>The State School of Higher Education in Chelm: Techsupport service.</h1>
	</main>
</body>
</html>

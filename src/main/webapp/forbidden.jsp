<!DOCTYPE html>
<html>
<head>

<title>Access is Forbidden</title>

<link rel="stylesheet"
	href="https://fonts.googleapis.com/css?family=Lato">
<style>



* {
  position: relative;
  margin: 3;
  padding: 0;
  box-sizing: border-box;
  font-family: 'Lato', sans-serif;
}

body {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(to bottom right, #EEE, #AAA);
}

h1 {
  margin: 40px 0 20px;
}

.lock {
  border-radius: 5px;
  width: 55px;
  height: 35px;
  background-color: #333;
  
  &::before,
  &::after {
    content: '';
    position: absolute;
    border-left: 5px solid #333;
    height: 20px;
    width: 15px;
    left: calc(50% - 12.5px);
  }
  
  &::before {
    top: -30px;
    border: 5px solid #333;
    border-bottom-color: transparent;
    border-radius: 15px 15px 0 0;
    height: 30px;
  }
  
  &::after {
    top: -10px; 
    border-right: 5px solid transparent;
  }
}

</style>
</head>

<body>
    <div class="lock"></div>
    <div class="message">
        <h1>Access to this page is restricted</h1>
        <p>Please check with the site admin if you believe this is a mistake.</p>
    </div>
</body>

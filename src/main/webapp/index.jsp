<!DOCTYPE html>
<html>
    <head>
        <title>Start Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script>
            var connection = new WebSocket('ws://localhost:8080${pageContext.request.contextPath}/sample-endpoint');

            connection.onopen = function () {
                console.log('Connected');
            };

            connection.onerror = function (error) {
                console.log('WebSocket Error: ' + error);
            };

            connection.onmessage = function (e) {
                document.getElementById('response').innerHTML = e.data
            };
        </script>
    </head>
    <body>
        Response: <span id="response"></span>
    </body>
</html>

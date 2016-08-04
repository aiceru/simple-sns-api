<%--
  Created by IntelliJ IDEA.
  User: iceru
  Date: 2016. 7. 29.
  Time: 오후 9:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean postId="hello" class="com.aiceru.lezhinapply.Hello"/>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <p>${hello.sayHello()}</p>
  </body>
</html>

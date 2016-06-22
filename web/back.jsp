<%-- 
    Document   : back
    Created on : 09.06.2016, 13:50:19
    Author     : alex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Wsockauth - Backend</title>
    </head>
    <body>
        <h1>Add User</h1>
        
        <form name="userreg" action="userreg.jsp" method="POST">
            <table border="0">
                <tbody>
                    <tr>
                        <td><strong>Логин:</strong></td>
                        <td>
                            <input type="text" name="user" value="" size="20" />
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Пароль:</strong></td>
                        <td>
                            <input type="password" name="password" value="" size="20" />
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Имя:</strong></td>
                        <td>
                            <input type="text" name="firstname" value="" size="20" />
                        </td>
                    </tr>
                    <tr>
                        <td><strong>Фамилия:</strong></td>
                        <td>
                            <input type="text" name="lastname" value="" size="20" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="submit" value="Зарегистрировать" name="registration" />
                        </td>
                    </tr>
                </tbody>
            </table>

        </form>
    </body>
</html>

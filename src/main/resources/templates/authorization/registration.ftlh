<!DOCTYPE html>
<html lang="en">
<head>
    <title>APartSystem</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            padding: 1% 5% 2% 5%;
        }
    </style>
</head>
<body>
<h1 class="mt3">APartSystem</h1>
<hr>
<h4>Регистрация</h4>
<form action="/registration" method="post">
    <div class="mb3">
        <label for="name" class="form-label">Имя:</label>
        <input type="text" id="name" name="name" class="form-control"
            <#if client?? && client.name??>
                value="${client.name}"
            </#if>>
        <#if validationErrors?? && validationErrors.getFieldError("name")??>
            <p style="color: red">${validationErrors.getFieldError("name").defaultMessage}</p>
        </#if>

    </div>
    <div class="mb3">
        <label for="email" class="form-label">Эл. почта:</label>
        <input type="email" id="email" name="email" class="form-control"
            <#if client?? && client.email??>
                value="${client.email}"
            </#if>
        >
        <#if validationErrors?? && validationErrors.getFieldError("email")??>
            <p style="color: red">${validationErrors.getFieldError("email").defaultMessage}</p>
        </#if>
    </div>
    <div class="mb3">
        <label for="phoneNumber" class="form-label">Номер телефона:</label>
        <input type="tel" id="phoneNumber" name="phoneNumber" class="form-control"
            <#if client??  && client.phoneNumber??>
                value="${client.phoneNumber}"
            </#if>
        >
        <#if validationErrors?? && validationErrors.getFieldError("phoneNumber")??>
            <p style="color: red">${validationErrors.getFieldError("phoneNumber").defaultMessage}</p>
        </#if>
    </div>
    <div class="mb3">
        <label for="password" class="form-label">Пароль:</label>
        <input type="password" class="form-control" id="password" name="password"
            <#if client?? && client.password??>
            value="${client.password}"
            </#if>
        >
        <#if validationErrors?? && validationErrors.getFieldError("password")??>
            <p style="color: red">${validationErrors.getFieldError("password").defaultMessage}</p>
        </#if>
    </div>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <button type="submit" class="btn btn-primary">Зарегистрироваться</button>
</form>
<#if errorMessage??>
    <h4 style="color: red">${errorMessage}</h4>
</#if>
</body>
</html>
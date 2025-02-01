<#include "../../header.ftlh">
<hr>
<h3>Обновить информацию о Крепеже:</h3>
<form action="/fasteners/edit/${fasteners.id}" method="post" enctype="multipart/form-data">
    <div class="mb-3">
        <label for="file1" class="form-label">Фотография:</label>
        <input type="file" class="form-control" id="file1" name="file1">
    </div>
    Название крепежа: <input type="text" name="name" value="${fasteners.name}"/><br><br>
    Количество: <input type="number" name="quantity" value="${fasteners.quantity}"/><br><br>
    Цена: <input type="number" step="0.01" name="price" value="${formattedPrice}"/><br><br>

    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <input type="submit" value="Сохранить" class="btn btn-primary"/>
</form>
<#if errorMessage??>
    <h2 style="color: red">${errorMessage}</h2>
</#if>
<#include "../../footer.ftlh">

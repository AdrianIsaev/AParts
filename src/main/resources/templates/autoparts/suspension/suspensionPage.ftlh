<#include "../../header.ftlh">
<hr>
<div class="row">
    <div class="col-md-6">
        <#if suspension.hasPreview()>
            <img src="/images/${suspension.previewImageId}" class="img-fluid rounded" alt="${suspension.name}">
        </#if>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Подвеска: ${suspension.name}</h3>
                </p>
                <p class="card-text">Осталось: ${suspension.quantity} шт.</p>
                <p class="card-text">Цена: ${suspension.price}₽</p>
                <#if user.isAdmin()>
                    <a href="/suspension/edit/${suspension.id}" class="btn btn-primary btn-sm">Редактировать</a>
                    <a href="/suspension/delete/${suspension.id}" class="btn btn-danger btn-sm">Удалить</a>
                </#if>
                <a href="/shopping/shoppingCart/addItem/${suspension.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
            </div>
        </div>
    </div>
</div>
<#include "../../footer.ftlh">

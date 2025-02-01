<#include "../../header.ftlh">
<hr>
<div class="row">
    <div class="col-md-6">
        <#if engine.hasPreview()>
            <img src="/images/${engine.previewImageId}" class="img-fluid rounded" alt="${engine.name}">
        </#if>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Двигатель: ${engine.name}</h3>
                </p>
                <p class="card-text">Осталось: ${engine.quantity} шт.</p>
                <p class="card-text">Цена: ${engine.price}₽</p>
                <#if user.isAdmin()>
                    <a href="/engine/edit/${engine.id}" class="btn btn-primary btn-sm">Редактировать</a>
                    <a href="/engine/delete/${engine.id}" class="btn btn-danger btn-sm">Удалить</a>
                </#if>
                <a href="/shopping/shoppingCart/addItem/${engine.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
            </div>
        </div>
    </div>
</div>
<#include "../../footer.ftlh">

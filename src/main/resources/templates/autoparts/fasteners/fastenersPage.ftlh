<#include "../../header.ftlh">
<hr>
<div class="row">
    <div class="col-md-6">
        <#if fasteners.hasPreview()>
            <img src="/images/${fasteners.previewImageId}" class="img-fluid rounded" alt="${fasteners.name}">
        </#if>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Крепёж: ${fasteners.name}</h3>
                </p>
                <p class="card-text">Осталось: ${fasteners.quantity} шт.</p>
                <p class="card-text">Цена: ${fasteners.price}₽</p>
                <#if user.isAdmin()>
                    <a href="/fasteners/edit/${fasteners.id}" class="btn btn-primary btn-sm">Редактировать</a>
                    <a href="/fasteners/delete/${fasteners.id}" class="btn btn-danger btn-sm">Удалить</a>
                </#if>
                <a href="/shopping/shoppingCart/addItem/${fasteners.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
            </div>
        </div>
    </div>
</div>
<#include "../../footer.ftlh">

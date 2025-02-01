<#include "../../header.ftlh">
<hr>
<div class="row">
    <div class="col-md-6">
        <#if chassis.hasPreview()>
            <img src="/images/${chassis.previewImageId}" class="img-fluid rounded" alt="${chassis.name}">
        </#if>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Ходовая: ${chassis.name}</h3>
                </p>
                <p class="card-text">Осталось: ${chassis.quantity} шт.</p>
                <p class="card-text">Цена: ${chassis.price}₽</p>
                <#if user.isAdmin()>
                    <a href="/chassis/edit/${chassis.id}" class="btn btn-primary btn-sm">Редактировать</a>
                    <a href="/chassis/delete/${chassis.id}" class="btn btn-danger btn-sm">Удалить</a>
                </#if>
                <a href="/shopping/shoppingCart/addItem/${chassis.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
            </div>
        </div>
    </div>
</div>
<#include "../../footer.ftlh">

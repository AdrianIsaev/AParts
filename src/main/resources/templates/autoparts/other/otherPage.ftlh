<#include "../../header.ftlh">
<hr>
<div class="row">
    <div class="col-md-6">
        <#if other.hasPreview()>
            <img src="/images/${other.previewImageId}" class="img-fluid rounded" alt="${other.name}">
        </#if>
    </div>
    <div class="col-md-6">
        <div class="card">
            <div class="card-body">
                <h3 class="card-title">Прочее: ${other.name}</h3>
                </p>
                <p class="card-text">Осталось: ${other.quantity} шт.</p>
                <p class="card-text">Цена: ${other.price}₽</p>
                <#if user.isAdmin()>
                    <a href="/other/edit/${other.id}" class="btn btn-primary btn-sm">Редактировать</a>
                    <a href="/other/delete/${other.id}" class="btn btn-danger btn-sm">Удалить</a>
                </#if>
                <a href="/shopping/shoppingCart/addItem/${other.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
            </div>
        </div>
    </div>
</div>
<#include "../../footer.ftlh">

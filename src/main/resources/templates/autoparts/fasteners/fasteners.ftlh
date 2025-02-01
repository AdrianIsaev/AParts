<#include "../../header.ftlh">
<hr>
<#if user.isActive()>
    <h3>Крепеж</h3>
    <#assign fastenersFound = false>
    <div class="row">
        <#list fasteners as fast>
            <#if fast.getAutoPartCategory() == "FASTENERS" && fast.quantity gt 0>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <#if fast.hasPreview()>
                            <img src="/images/${fast.previewImageId}" class="card-img-top" alt="${fast.name}">
                        </#if>
                        <div class="card-body">
                            <h5 class="card-title">${fast.name}</h5>
                            <p class="card-text">
                                Крепеж ${fast.price}₽ | Количество: ${fast.quantity}
                            </p>
                            <#if user.isAdmin()>
                                <a href="/fasteners/edit/${fast.id}" class="btn btn-primary btn-sm">Редактировать</a>
                                <a href="/fasteners/delete/${fast.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </#if>
                            <a href="/shopping/shoppingCart/addItem/${fast.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
                            <a href="/fasteners/${fast.id}" class="btn btn-outline-info btn-sm">Подробнее...</a>
                        </div>
                    </div>
                </div>
                <#assign fastenersFound = true>
            </#if>
        </#list>
    </div>
</#if>
<#include "../../footer.ftlh">
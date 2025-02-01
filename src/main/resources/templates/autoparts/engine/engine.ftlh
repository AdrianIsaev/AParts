<#include "../../header.ftlh">
<hr>
<#if user.isActive()>
    <h3>Двигатели</h3>
    <#assign engineFound = false>
    <div class="row">
        <#list engines as eng>
            <#if eng.getAutoPartCategory() == "ENGINE" && eng.quantity gt 0>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <#if fast.hasPreview()>
                            <img src="/images/${eng.previewImageId}" class="card-img-top" alt="${eng.name}">
                        </#if>
                        <div class="card-body">
                            <h5 class="card-title">${eng.name}</h5>
                            <p class="card-text">
                                Двигатель ${eng.price}₽ | Количество: ${eng.quantity}
                            </p>
                            <#if user.isAdmin()>
                                <a href="/engine/edit/${eng.id}" class="btn btn-primary btn-sm">Редактировать</a>
                                <a href="/engine/delete/${eng.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </#if>
                            <a href="/shopping/shoppingCart/addItem/${eng.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
                            <a href="/engine/${eng.id}" class="btn btn-outline-info btn-sm">Подробнее...</a>
                        </div>
                    </div>
                </div>
                <#assign engineFound = true>
            </#if>
        </#list>
    </div>
</#if>
<#include "../../footer.ftlh">
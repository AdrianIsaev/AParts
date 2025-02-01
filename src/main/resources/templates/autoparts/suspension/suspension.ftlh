<#include "../../header.ftlh">
<hr>
<#if user.isActive()>
    <h3>Подвеска</h3>
    <#assign suspensionFound = false>
    <div class="row">
        <#list suspension as susp>
            <#if susp.getAutoPartCategory() == "SUSPENSION" && susp.quantity gt 0>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <#if susp.hasPreview()>
                            <img src="/images/${susp.previewImageId}" class="card-img-top" alt="${susp.name}">
                        </#if>
                        <div class="card-body">
                            <h5 class="card-title">${susp.name}</h5>
                            <p class="card-text">
                                Подвеска ${susp.price}₽ | Количество: ${susp.quantity}
                            </p>
                            <#if user.isAdmin()>
                                <a href="/suspension/edit/${susp.id}" class="btn btn-primary btn-sm">Редактировать</a>
                                <a href="/suspension/delete/${susp.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </#if>
                            <a href="/shopping/shoppingCart/addItem/${susp.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
                            <a href="/suspension/${susp.id}" class="btn btn-outline-info btn-sm">Подробнее...</a>
                        </div>
                    </div>
                </div>
                <#assign suspensionFound = true>
            </#if>
        </#list>
    </div>
</#if>

<#include "../../footer.ftlh">
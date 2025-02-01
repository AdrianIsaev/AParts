<#include "../../header.ftlh">
<hr>
<#if user.isActive()>
    <h3>Прочее</h3>
    <#assign otherFound = false>
    <div class="row">
        <#list others as oth>
            <#if oth.getAutoPartCategory() == "OTHER" && oth.quantity gt 0>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <#if oth.hasPreview()>
                            <img src="/images/${oth.previewImageId}" class="card-img-top" alt="${oth.name}">
                        </#if>
                        <div class="card-body">
                            <h5 class="card-title">${oth.name}</h5>
                            <p class="card-text">
                                Прочее ${oth.price}₽ | Количество: ${oth.quantity}
                            </p>
                            <#if user.isAdmin()>
                                <a href="/other/edit/${oth.id}" class="btn btn-primary btn-sm">Редактировать</a>
                                <a href="/other/delete/${oth.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </#if>
                            <a href="/shopping/shoppingCart/addItem/${oth.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
                            <a href="/other/${oth.id}" class="btn btn-outline-info btn-sm">Подробнее...</a>
                        </div>
                    </div>
                </div>
                <#assign otherFound = true>
            </#if>
        </#list>
    </div>
</#if>

<#include "../../footer.ftlh">
<#include "../../header.ftlh">
<hr>
<#if user.isActive()>
    <h3>Ходовая</h3>
    <#assign chassisFound = false>
    <div class="row">
        <#list chassis as chass>
            <#if chass.getAutoPartCategory() == "CHASSIS" && chass.quantity gt 0>
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <#if chass.hasPreview()>
                            <img src="/images/${chass.previewImageId}" class="card-img-top" alt="${chass.name}">
                        </#if>
                        <div class="card-body">
                            <h5 class="card-title">${chass.name}</h5>
                            <p class="card-text">
                                Ходовая ${chass.price}₽ | Количество: ${chass.quantity}
                            </p>
                            <#if user.isAdmin()>
                                <a href="/chassis/edit/${chass.id}" class="btn btn-primary btn-sm">Редактировать</a>
                                <a href="/chassis/delete/${chass.id}" class="btn btn-danger btn-sm">Удалить</a>
                            </#if>
                            <a href="/shopping/shoppingCart/addItem/${chass.id}" class="btn btn-success btn-sm">Добавить в корзину</a>
                            <a href="/chassis/${chass.id}" class="btn btn-outline-info btn-sm">Подробнее...</a>
                        </div>
                    </div>
                </div>
                <#assign chassisFound = true>
            </#if>
        </#list>
    </div>
</#if>

<#include "../../footer.ftlh">
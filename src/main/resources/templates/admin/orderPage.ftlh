<#include "../header.ftlh">

<style>
    .cart-button {
        display: inline-block;
        padding: 10px 15px;
        margin: 5px;
        font-size: 45px;
        text-align: center;
        text-decoration: none;
        cursor: pointer;
        border-radius: 5px;
        transition: background-color 0.3s ease-in-out;
        min-width: 300px;
    }

    .cart-button.accept {
        background-color: #2196F3; /* Blue */
        color: white;
    }

    .cart-button.cancel {
        background-color: #f44336; /* Red */
        color: white;
    }

    .cart-button.cooked {
        background-color: #4CAF50; /* Green */
        color: white;
    }

    .order-actions {
        display: flex;
        flex-direction: column-reverse; /* Updated to column-reverse */
        text-align: right;
        margin-left: 20px;
    }
</style>

<hr>

<#if user.isActive()>
    <div class="order-card"
         style="font-size: 20px; border: 1px solid #ccc; padding: 10px; margin-bottom: 20px; display: flex; justify-content: space-between; align-items: stretch;">
        <div style="flex-grow: 1;">
            <h3>Заказ № ${order.getId()}</h3>
            Дата: ${user.creationDate.toLocalDate()} /
            ${user.creationDate.getHour()}:${user.creationDate.getMinute()}:${user.creationDate.getSecond()}
            <br>
            Адрес: ${order.address}
            <br>
            Статус: <#list order.status as status>
                <#if status=="CREATED">
                    Создан
                <#elseif status=="ACCEPTED">
                    Принят
                <#elseif status=="ASSEMBLED">
                    Собран
                <#elseif status=="DELIVERING">
                    Доставляется
                <#elseif status=="COMPLETED">
                    Завершен
                <#elseif status=="CANCELED">
                    Отменен
                <#else>
                    ${status}<br>
                </#if>
            </#list>
            <br>
            Имя заказчика: ${order.client.name}
            <br>
            Телефон: ${order.client.phoneNumber}
            <br>
            Почта: ${order.client.email}
            <br>
            <br>
            <h3>Список заказа:</h3>
            <#list order.getItems() as autoparts, quantity>
                <div class="product-card"
                     style="display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #ccc; padding: 10px;">
                    <#if autoparts.images?has_content>
                        <img src="/images/${autoparts.previewImageId}" alt="Изображение запчасти"
                             style="width: 150px; height: auto;">
                    </#if>
                    <div style="flex-grow: 1; display: flex; justify-content: space-between; align-items: center;">
                        <div style="display: flex; flex-direction: column; align-items: flex-start; margin: 20px;">
                            <h3>
                                <#if autoparts.autoPartCategory == "CHASSIS">
                                    Ходовая
                                </#if>
                                <#if autoparts.autoPartCategory == "FASTENERS">
                                    Крепёж
                                </#if>
                                <#if autoparts.autoPartCategory == "SUSPENSION">
                                    Подвеска
                                </#if>
                                <#if autoparts.autoPartCategory == "OTHER">
                                    Прочее
                                </#if>
                                <#if autoparts.autoPartCategory == "ENGINE">
                                    Двигатель
                                </#if>
                                ${autoparts.name}
                            </h3>
                            <h4>Количество: ${quantity}</h4>
                        </div>
                    </div>
                </div>
            </#list>
        </div>
    </div>
</#if>

<#include "../footer.ftlh">

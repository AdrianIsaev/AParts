<#include "../header.ftlh">
<hr>
<h3>Панель администратора:</h3>
<div class="row">
    <div class="col-md-4">
        <a href="/fasteners/panel" class="btn btn-info btn-block">Список крепежей</a>
        <a href="/chassis/panel" class="btn btn-info btn-block">Список ходовых</a>
        <a href="/engine/panel" class="btn btn-info btn-block">Список двигателей</a>
    </div>
    <hr>
    <div class="col-md-4">
        <a href="/suspension/panel" class="btn btn-info btn-block">Список подвесок</a>
        <a href="/other/panel" class="btn btn-info btn-block">Прочее</a>
    </div>
    <hr>
    <div class="col-md-4">
        <a href="/admin/users" class="btn btn-success btn-block">Список пользователей</a>
        <a href="/admin/orders" class="btn btn-success btn-block">Список заказов</a>
    </div>
</div>
<#include "../footer.ftlh">
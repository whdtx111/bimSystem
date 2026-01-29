<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>导出模板</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #000;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
<h1>导出数据</h1>
<table>
    <thead>
    <tr>
        <th>WBS Name</th>
        <th>WBS Code</th>
        <th>Element ID</th>
        <th>Type</th>
        <th>Name</th>
        <th>Value</th>
        <th>Unit</th>
        <th>Category</th>
    </tr>
    </thead>
    <tbody>
    <#list resList as item>
        <tr>
            <td>${item.wbsName!}</td>
            <td>${item.wbsCode!}</td>
            <td>${item.elementId!}</td>
            <td>${item.type!}</td>
            <td>${item.name!}</td>
            <td>${item.value!}</td>
            <td>${item.unit!}</td>
            <td>${item.category!}</td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
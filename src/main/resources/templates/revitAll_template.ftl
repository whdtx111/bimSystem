<#-- Freemarker Template for FinalExcel01 Data -->
<#-- 表头 -->
<table>
    <tr>
        <th>WBS Name</th>
        <th>Name</th>
        <th>Value</th>
        <th>Unit</th>
        <th>Data Type</th>
        <th>Detail</th>
    </tr>

    <#-- 数据行 -->
    <#list dataList as data>
        <tr>
            <td>${data.wbsName!}</td>
            <td>${data.name!}</td>
            <td>${data.value!}</td>
            <td>${data.unit!}</td>
            <td>${data.dataType!}</td>
            <td>${data.detail!}</td>
        </tr>
    </#list>
</table>

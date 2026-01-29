<?xml version="1.0" encoding="UTF-8"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet">
    <Worksheet ss:Name="WBS Parameters">
        <Table>
            <!-- 表头 -->
            <Row>
                <Cell><Data ss:Type="String">ID</Data></Cell>
                <Cell><Data ss:Type="String">WBS ID</Data></Cell>
                <Cell><Data ss:Type="String">PID</Data></Cell>
                <Cell><Data ss:Type="String">WBS Code</Data></Cell>
                <Cell><Data ss:Type="String">Category</Data></Cell>
                <Cell><Data ss:Type="String">WBS Name</Data></Cell>
                <Cell><Data ss:Type="String">WBS Level</Data></Cell>
                <Cell><Data ss:Type="String">Detail</Data></Cell>
                <Cell><Data ss:Type="String">Modify User</Data></Cell>
                <Cell><Data ss:Type="String">Status</Data></Cell>
            </Row>
            <!-- 数据行 -->
            <#list wbsList as wbs>
                <Row>
                    <Cell><Data ss:Type="String">${wbs.id}</Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.wbsId}</Data></Cell>
                    <Cell><Data ss:Type="String"><#if wbs.pid??>
                                ${wbs.pid}
                            <#else>
                                " "
                            </#if></Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.wbsCode}</Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.category?join(",")}</Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.wbsName}</Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.wbsLv}</Data></Cell>
                    <Cell><Data ss:Type="String"><#if wbs.detail??>
                                ${wbs.detail}
                            <#else>
                                " "
                            </#if></Data></Cell>
                    <Cell><Data ss:Type="String">${wbs.modifyUser}</Data></Cell>
                    <Cell><Data ss:Type="Number">${wbs.wbsStatus}</Data></Cell>
                </Row>
            </#list>
        </Table>
    </Worksheet>
</Workbook>


<#macro renderBeanDetails>
    <table class="table table-sm table-borderless table-striped mb-3 mt-3">
        <caption>Bean Details</caption>
        <tr>
            <th scope="row">Domain</th>
            <td class="text-monospace">${bean.domain}</td>
        </tr>
        <tr>
            <th scope="row">Key Properties</th>
            <td class="text-monospace">${bean.keyProperties}</td>
        </tr>
        <tr>
            <th scope="row">Description</th>
            <td>${bean.description!""}</td>
        </tr>
    </table>
</#macro>

<#macro renderAttributeDetails>
    <table class="table table-sm table-borderless table-striped mb-3 mt-3">
        <caption>Attribute Details</caption>
        <tr>
            <th scope="row">Name</th>
            <td class="text-monospace">${attribute.name}</td>
        </tr>
        <tr>
            <th scope="row">Description</th>
            <td>${attribute.description!""}</td>
        </tr>
        <tr>
            <th scope="row">Type</th>
            <td>
                <code>${attribute.type}</code>
            </td>
        </tr>
    </table>
</#macro>

<#macro renderOperationDetails>
    <table class="table table-sm table-borderless table-striped mb-3 mt-3">
        <caption>Operation Details</caption>
        <tr>
            <th scope="row">Name</th>
            <td class="text-monospace">${operation.name}</td>
        </tr>
        <tr>
            <th scope="row">Description</th>
            <td>${operation.description!""}</td>
        </tr>
    </table>
</#macro>

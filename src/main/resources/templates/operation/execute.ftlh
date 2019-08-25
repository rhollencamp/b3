<#import "../lib/details.ftlh" as details>
<#import "../lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name, urlGenerator.app(app)),
                      breadcrumb(bean.keyProperties, urlGenerator.bean(app, domain, keyProperties)),
                      breadcrumb(operation.name, urlGenerator.operation(app, domain, keyProperties, operation)),
                      breadcrumb("Execution Results")]/>

<@page.wrap title="Execution Results" breadcrumbs=breadcrumbs>
    <@details.renderBeanDetails/>
    <@details.renderOperationDetails/>

    <table class="table table-sm table-borderless table-striped mt-1">
        <caption>Execution Results</caption>
        <thead>
            <tr>
                <th scope="col">Node</th>
                <th scope="col">Result</th>
            </tr>
        </thead>
        <tbody>
            <#list stdout as resultApp, resultText>
                <tr>
                    <td>
                        <a href="${urlGenerator.app(resultApp)}">${resultApp.name}</a>
                    </td>
                    <td class="text-monospace" style="white-space: pre;">${resultText}</td>
                </tr>
            </#list>
        </tbody>
    </table>
</@page.wrap>

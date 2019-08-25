<#import "../lib/details.ftlh" as details>
<#import "../lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name, urlGenerator.app(app)),
                      breadcrumb(bean.domain, urlGenerator.domain(app, domain)),
                      breadcrumb(bean.keyProperties, urlGenerator.bean(app, domain, keyProperties)),
                      breadcrumb(attribute.name, urlGenerator.attribute(app, domain, keyProperties, attribute)),
                      breadcrumb("Write Results")]/>

<@page.wrap title="Write Results" breadcrumbs=breadcrumbs>
    <@details.renderBeanDetails/>
    <@details.renderAttributeDetails/>

    <table class="table table-sm table-borderless table-striped mt-1">
        <caption>Read Results</caption>
        <thead>
            <tr>
                <th scope="col">Node</th>
                <th scope="col">Result</th>
            </tr>
        </thead>
        <tbody>
            <#list results as resultApp, result>
                <tr>
                    <td>
                        <a href="${urlGenerator.app(resultApp)}">${resultApp.name}</a>
                    </td>
                    <td class="text-monospace">${result}</td>
                </tr>
            </#list>
        </tbody>
    </table>
</@page.wrap>

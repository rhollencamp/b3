<#import "lib/details.ftlh" as details>
<#import "lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name, urlGenerator.app(app)),
                      breadcrumb(bean.domain, urlGenerator.domain(app, bean.domain)),
                      breadcrumb(bean.keyProperties)]/>

<@page.wrap title="${bean.domain}:${bean.keyProperties}" breadcrumbs=breadcrumbs>
    <@details.renderBeanDetails/>

    <#--
      - Attributes
      -->
    <#if bean.attributes?has_content>
        <table class="table table-sm table-borderless table-striped mt-3">
            <caption>Attributes</caption>
            <thead>
                <tr>
                    <td>Name</td>
                    <td>Type</td>
                    <td>Description</td>
                    <td>Value</td>
                    <td>RW</td>
                </tr>
            </thead>
            <#list bean.attributes as attribute>
                <tr>
                    <td class="text-nowrap">
                        <a href="${urlGenerator.attribute(app, domain, keyProperties, attribute)}">
                            ${attribute.name}
                        </a>
                    </td>
                    <td class="text-nowrap"><code>${attribute.type}</code></td>
                    <td>${attribute.description!""}</td>
                    <td class="text-monospace">${attributes[attribute.name]!"null"}</td>
                    <td>
                        ${attribute.readable?then("R", "")}${attribute.writable?then("W", "")}
                    </td>
                </tr>
            </#list>
        </table>
    </#if>

    <#if bean.operations?has_content>
        <table class="table table-sm table-borderless table-striped mt-3">
            <caption>Operations</caption>
            <thead>
            <tr>
                <td>Name</td>
                <td>Return Type</td>
                <td>Description</td>
                <td>Parameters</td>
            </tr>
            </thead>
            <#list bean.operations as operation>
                <tr>
                    <td class="text-nowrap">
                        <a href="${urlGenerator.operation(app, domain, keyProperties, operation)}">
                            ${operation.name}
                        </a>
                    </td>
                    <td class="text-nowrap"><code>${operation.returnType}</code></td>
                    <td>${operation.description!""}</td>
                    <td>
                        <#list operation.parameters as param>
                            <div class="text-nowrap"><code>${param.type}</code> ${param.name}</div>
                        </#list>
                    </td>
                </tr>
            </#list>
        </table>
    </#if>
</@page.wrap>

<#import "lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name)]/>

<@page.wrap title=app.name breadcrumbs=breadcrumbs>
    <table class="table table-sm table-borderless table-striped">
        <thead>
            <tr>
                <th>Domain</th>
                <th>Key Properties</th>
            </tr>
        </thead>
        <tbody>
            <#list beans?keys?sort as domain>
                <#list beans[domain]?sort_by("keyProperties") as bean>
                    <tr>
                        <td>
                            <a href="${urlGenerator.domain(app, domain)}">
                                ${domain}
                            </a>
                        </td>
                        <td>
                            <a href="${urlGenerator.bean(app, domain, bean.keyProperties)}">
                                ${bean.keyProperties}
                            </a>
                        </td>
                    </tr>
                </#list>
            </#list>
        </tbody>
    </table>
</@page.wrap>

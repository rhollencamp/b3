<#import "lib/wrap.ftlh" as page/>

<@page.wrap title="" breadcrumbs=[]>
    <table class="table table-sm table-striped table-borderless">
        <thead>
            <tr>
                <th scope="col">Cluster</th>
                <th scope="col">Name</th>
                <th scope="col">Status</th>
            </tr>
        </thead>
        <tbody>
            <#list clusters as cluster>
                <#list cluster.apps as app>
                    <#assign healthy=healthyNodes?seq_contains(app)/>
                    <tr>
                        <td>
                            <a href="${urlGenerator.cluster(cluster)}">${cluster.name}</a>
                        </td>
                        <td>
                            <#if healthy>
                                <a href="${urlGenerator.app(app)}">${app.name}</a>
                            <#else>
                                ${app.name}
                            </#if>
                        </td>
                        <td>
                            <span class="mr-2 badge badge-pill ${healthy?then("badge-success", "badge-danger")}">
                                ${healthy?then("UP", "DOWN")}
                            </span>
                        </td>
                    </tr>
                </#list>
            </#list>
        </tbody>
    </table>
</@page.wrap>

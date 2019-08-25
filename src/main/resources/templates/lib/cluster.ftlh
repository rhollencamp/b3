<#import "card.ftlh" as card/>

<#macro render cluster hasNext=false>
    <@card.render title=cluster.name list=true margin=hasNext?then("mb-3", "mb-0")>
        <#list cluster.apps as app>
            <#assign healthy=healthyNodes?seq_contains(app)/>
            <#assign url=urlGenerator.app(app)/>
            <a href="${url}" class="list-group-item list-group-item-action d-flex align-items-center ${healthy?then("text-primary", "disabled")}">
                    <span class="mr-2 badge badge-pill ${healthy?then("badge-success", "badge-danger")}">
                        &nbsp;
                    </span>
                ${app.name}
            </a>
        </#list>
    </@card.render>
</#macro>

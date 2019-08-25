<#macro render title cardClassExtra="" bodyClassExtra="" sticky=false margin="mb-3" list=false table=false>
    <div class="card ${margin} border-secondary ${cardClassExtra}">
        <#assign style=sticky?then("style=\"position: sticky; top: 0; z-index: 9999;\"", "")/>
        <div class="card-header border-secondary bg-light" ${style?no_esc}>${title}</div>
        <div class="card-body border-secondary ${list?then("list-group list-group-flush p-0", "")} ${table?then("p-0", "")} ${bodyClassExtra}">
            <#nested />
        </div>
    </div>
</#macro>

<#import "../lib/details.ftlh" as details>
<#import "../lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name, urlGenerator.app(app)),
                      breadcrumb(bean.keyProperties, urlGenerator.bean(app, domain, keyProperties)),
                      breadcrumb(operation.name)]/>

<@page.wrap title=operation.name breadcrumbs=breadcrumbs>
    <@details.renderBeanDetails/>
    <@details.renderOperationDetails/>

    <div class="mt-3 mb-3" id="operation-operations">
        <div class="card">
            <div class="card-header" id="operation-execute">
                <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#operation-execute-body" aria-expanded="true" aria-controls="operation-execute-body">
                    Execute
                </button>
            </div>
            <div id="operation-execute-body" class="collapse" aria-labelledby="operation-execute" data-parent="#operation-operations">
                <div class="card-body clearfix">
                    <form action="/operation" method="POST">
                        <input type="hidden" name="cluster" value="${app.cluster.name}"/>
                        <input type="hidden" name="app" value="${app.name}"/>
                        <input type="hidden" name="domain" value="${domain}"/>
                        <input type="hidden" name="keyProperties" value="${keyProperties}"/>
                        <input type="hidden" name="operation" value="${operation.name}"/>
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

                        <div class="form-group row">
                            <label for="write-target" class="col-2 col-form-label">Target</label>
                            <div class="col-10">
                                <select id="write-target" name="target" class="form-control">
                                    <option value="app">${app.cluster.name} / ${app.name}</option>
                                    <option value="cluster">${app.cluster.name} / *</option>
                                </select>
                            </div>
                        </div>

                        <#if operation.parameters?has_content>
                            <div class="form-group row">
                                <label class="col-2 col-form-label">Parameters</label>
                                <div class="col-10">
                                    <div class="card">
                                        <table class="table table-borderless table-striped table-sm mb-0">
                                            <thead>
                                                <tr>
                                                    <th scope="col">Name</th>
                                                    <th scope="col">Description</th>
                                                    <th scope="col">Type</th>
                                                    <th scope="col">Value</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <#list operation.parameters as param>
                                                    <tr>
                                                        <td class="text-monospace">${param.name}</td>
                                                        <td>${param.description!""}</td>
                                                        <td><code>${param.type}</code></td>
                                                        <td>
                                                            <input type="hidden" name="signature" value="${param.type}"/>
                                                            <input class="form-control" name="argument"/>
                                                        </td>
                                                    </tr>
                                                </#list>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </#if>

                        <button type="submit" class="btn btn-primary float-right">Execute</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</@page.wrap>

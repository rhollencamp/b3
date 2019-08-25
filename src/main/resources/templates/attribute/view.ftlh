<#import "../lib/details.ftlh" as details>
<#import "../lib/wrap.ftlh" as page>

<#assign breadcrumbs=[breadcrumb("Home", "/"),
                      breadcrumb(app.cluster.name, urlGenerator.cluster(app.cluster)),
                      breadcrumb(app.name, urlGenerator.app(app)),
                      breadcrumb(bean.domain, urlGenerator.domain(app, bean.domain)),
                      breadcrumb(bean.keyProperties, urlGenerator.bean(app, domain, keyProperties)),
                      breadcrumb(attribute.name)]/>

<@page.wrap title=attribute.name breadcrumbs=breadcrumbs>
    <@details.renderBeanDetails/>
    <@details.renderAttributeDetails/>

    <#if (attribute.readable || attribute.writable)>
        <div class="accordion mt-3 mb-3" id="attribute-operations">
            <div class="card">
                <div class="card-header" id="attribute-read">
                    <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#attribute-read-body" aria-expanded="true" aria-controls="attribute-read-body">
                        Read Value
                    </button>
                </div>
                <div id="attribute-read-body" class="collapse" aria-labelledby="attribute-read" data-parent="#attribute-operations">
                    <div class="card-body clearfix">
                        <form action="/attribute" method="GET">
                            <input type="hidden" name="cluster" value="${app.cluster.name}"/>
                            <input type="hidden" name="app" value="${app.name}"/>
                            <input type="hidden" name="domain" value="${domain}"/>
                            <input type="hidden" name="keyProperties" value="${keyProperties}"/>
                            <input type="hidden" name="attribute" value="${attribute.name}"/>

                            <div class="form-group row">
                                <label for="read-target" class="col-2 col-form-label">Target</label>
                                <div class="col-10">
                                    <select id="read-target" name="target" class="form-control">
                                        <option value="app">${app.cluster.name} / ${app.name}</option>
                                        <option value="cluster">${app.cluster.name} / *</option>
                                    </select>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary float-right">View</button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="card">
                <div class="card-header" id="attribute-write">
                    <button class="btn btn-link collapsed" type="button" data-toggle="collapse" data-target="#attribute-write-body" aria-expanded="false" aria-controls="attribute-write-body">
                        Write Value
                    </button>
                </div>
                <div id="attribute-write-body" class="collapse" aria-labelledby="attribute-write" data-parent="#attribute-operations">
                    <div class="card-body clearfix">
                        <form action="/attribute" method="POST">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <input type="hidden" name="cluster" value="${app.cluster.name}"/>
                            <input type="hidden" name="app" value="${app.name}"/>
                            <input type="hidden" name="domain" value="${domain}"/>
                            <input type="hidden" name="keyProperties" value="${keyProperties}"/>
                            <input type="hidden" name="attribute" value="${attribute.name}"/>

                            <div class="form-group row">
                                <label for="write-value" class="col-2 col-form-label">Value</label>
                                <div class="col-10">
                                    <input id="write-value" name="value" class="form-control" required/>
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="write-target" class="col-2 col-form-label">Target</label>
                                <div class="col-10">
                                    <select id="write-target" name="target" class="form-control">
                                        <option value="app">${app.cluster.name} / ${app.name}</option>
                                        <option value="cluster">${app.cluster.name} / *</option>
                                    </select>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary float-right">Set</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </#if>
</@page.wrap>

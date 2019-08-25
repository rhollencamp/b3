<#macro renderBreadcrumbs breadcrumbs>
    <#if breadcrumbs?has_content>
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <#list breadcrumbs as breadcrumb>
                    <li class="breadcrumb-item ${breadcrumb.url???then("", "active")}">
                        <a ${breadcrumb.url???then("href=\"" + breadcrumb.url + "\"", "")?no_esc}>
                            <#if breadcrumb.title?length gt 20>
                                ${breadcrumb.title[0..20]}...
                            <#else>
                                ${breadcrumb.title}
                            </#if>
                        </a>
                    </li>
                </#list>
            </ol>
        </nav>
    </#if>
</#macro>

<#macro wrap title breadcrumbs>
    <!doctype html>
    <html lang="en" style="overflow-y:scroll;font-size:14px;">
        <head>
            <meta charset="utf-8">
            <meta http-equiv="Cache-Control" content="no-store"/>
            <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
            <link rel="stylesheet" href="/webjars/bootstrap/4.3.1/css/bootstrap.min.css">
            <title>Percolator${title?has_content?then(" - ${title}", "")}</title>

            <style type="text/css">
                pre {
                    margin: 0;
                }

                table.table caption {
                    caption-side: top;
                    font-weight: bold;
                    color: black;
                    padding: 0;
                    font-size: 1.5rem;
                }
            </style>
        </head>
        <body>
            <main role="main" class="container" style="max-width:1500px;">

                <nav class="navbar navbar-expand-md navbar-dark bg-dark mb-4">
                    <a class="navbar-brand mr-auto" href="/">Percolator</a>

                    <#if getPrincipal() != "anonymousUser">
                        <form class="form-inline" action="/logout" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                            <button class="btn btn-primary" type="submit">Logout</button>
                        </form>
                    </#if>
                </nav>

                <@renderBreadcrumbs breadcrumbs=breadcrumbs/>

                <#nested/>
            </main>

            <script src="/webjars/jquery/3.0.0/jquery.min.js"></script>
            <script src="/webjars/popper.js/1.14.3/umd/popper.min.js"></script>
            <script src="/webjars/bootstrap/4.3.1/js/bootstrap.min.js"></script>
        </body>
    </html>
</#macro>

<!DOCTYPE html>
<html lang="en"><head>



<script type="text/javascript" src="js/script.js"></script>


</script>

<meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="https://getbootstrap.com/favicon.ico">

    <title>Accio</title>

   <!-- Bootstrap core CSS -->
            <link rel='stylesheet' href='<%= org.webjars.AssetLocator.getWebJarPath("bootstrap.css") %>'>
            <link rel='stylesheet' href='<%= org.webjars.AssetLocator.getWebJarPath("ie10-viewport-bug-workaround.css") %>'>
            <link rel='stylesheet' href='<%= org.webjars.AssetLocator.getWebJarPath("starter-template.css") %>'>


  </head>

  <body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Accio</a>
        </div>
      </div>
    </nav>

    <br></br>

    <div class="container">
        <div class="row">
            <center><h2>Le Quack Accio Searcher</h2></center>
            <div id="custom-search-input">
                <div class="input-group col-md-12">
                    <input type="text" class="  search-query form-control" placeholder="Search" id = 'value'/>
                                <span class="input-group-btn">
                                    <button class="btn btn-primary" type="button" onClick="queryAccio();">
                                        Accio
                                    </button>
                                </span>

                                <span class="input-group-btn">
                                      <button class="btn btn-success" type="button" onClick="queryLucene();">
                                          Lucene
                                       </button>
                                </span>


                </div>


            </div>
            <h2 id="queries"> </h2>


        </div>
    </div>

 <link rel='stylesheet' href='<%= org.webjars.AssetLocator.getWebJarPath("bootstrap/bootstrap.min.css") %>'>
  <link rel='stylesheet' href='<%= org.webjars.AssetLocator.getWebJarPath("css/bootstrap-theme.min.css") %>'>
 <script type='text/javascript' src='<%= org.webjars.AssetLocator.getWebJarPath("jquery.min.js") %>'></script>
  <script type='text/javascript' src='<%= org.webjars.AssetLocator.getWebJarPath("js/bootstrap.min.js") %>'></script>

</body></html>


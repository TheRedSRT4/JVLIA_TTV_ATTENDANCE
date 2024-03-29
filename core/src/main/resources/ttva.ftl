<!DOCTYPE html>
<html lang="en">
<head>
    <title>${streamer} - TTVAttendance</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <link href="https://unpkg.com/tabulator-tables@5.1.0/dist/css/tabulator.min.css" rel="stylesheet">
    <link href="https://unpkg.com/tabulator-tables@5.1.0/dist/css/tabulator_bootstrap4.min.css" rel="stylesheet">
    <script type="text/javascript" src="https://unpkg.com/tabulator-tables@5.1.0/dist/js/tabulator.min.js"></script>
    <!-- Panelbear -->
    <script async src="https://cdn.panelbear.com/analytics.js?site=9YoL3LJneKU"></script>
    <script>
        window.panelbear = window.panelbear || function() { (window.panelbear.q = window.panelbear.q || []).push(arguments); };
        panelbear('config', { site: '9YoL3LJneKU' });
    </script>
</head>
<body>
<div class="position-absolute top-0 start-0">
    <a href="https://twitch.tv/theredsrt4">
        <img src="https://i.imgur.com/EUVQdsS.png" width="100px" alt="logo">
    </a>
</div>
<div style="position: absolute; right: 0; top: 0; ">
    <a target="_blank" href="https://betteruptime.com/"><img style="width: 130px; height: 52px;" alt="Better Uptime Website Monitoring" src="https://betteruptime.com/assets/static_assets/badges/dark.png" /></a>
</div>

<div class="text-center mb-5">
    <h1 class="display-3"><b>Twitch Attendance Chart</b></h1>
    <h1 class="display-4">jvlia - <small>Season 2</small></h1>
    <a href="s1/"><p> View Season 1 Results</p></a>
    <p>updates every 2 min</p>
    <p>Started Tracking on January 1st, 2023</p>
    <p>*Estimated minutes, some might be a couple of minutes in offline chat.*</p>
</div>

<div class="position-absolute top-30 start-50 translate-middle-x" style="height: 70%">
    <div class="input-group mb-3">
        <span class="input-group-text" id="search-text">Search:</span>
        <input type="text" class="form-control" id="search" aria-label="Search by name" aria-describedby="search-text" placeholder="search by name" autocomplete="off">
    </div>
    <div id="table"></div>
</div>

<script>
    const search = document.getElementById("search");
    const table = new Tabulator("#table", {
        height: "100%",
        ajaxURL: "data/",
        progressiveLoad: "load",
        paginationSize: 10000, // amount of rows per page from ttva
        ajaxURLGenerator: function(url, config, params) {
            return url + params.page.toString() + ".json";
        },
        autoColumns: true
    });

    table.on("dataProcessed", function() {
        table.setSort("present", "desc");
    });

    search.addEventListener("keyup", function() {
        table.setFilter("name", "like", search.value);
    });
</script>
</body>
</html>

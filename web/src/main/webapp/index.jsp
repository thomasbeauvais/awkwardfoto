<html>
<head>
    <title>Gallery Page</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script src="/js/jquery.lazyload.js"></script>

    <script>
        function elementloaded( element ) {
            console.log( element );
        }

        $(document).ready(function () {
            $.getJSON('/debug/list', function(response) {
                var container = $("<div/>", {id:"gallery"}).appendTo($("body"));

                for (var i = 0; i < response.length; i++) {
                    var obj = response[ i ];

                    var url = '/image/download?key=' + obj.blobKey + '&height=' + container.height();
                    var clr = '/debug/scale?key=' + obj.blobKey + '&height=' + container.height();
                    $("<img/>", {
                        'src': clr,
                        'data-original': url,
                        'height': '100%',
                        'class': 'lazy'
                    }).appendTo(container);
                }

                var object = $("img.lazy").show().lazyload({
//                        threshold: 500,
                    effect: 'fadeIn',
                    effectspeed: 2000,
                    elementloaded: elementloaded,
                    container: container
                });
            });
        });
    </script>
    <style>
        body {
        }

        #gallery {
            width: 100%;
            height: 100%;
            overflow: auto;
            white-space: nowrap;
        }

        .lazy {
            display: none;
            /*border-style: dashed;*/
        }
    </style>
</head>
<body>
<%--<div id="gallery"/>--%>
</body>
</html>

<html>
<head>
    <title>Gallery Page</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script src="/js/jquery.lazyload.js"></script>

    <script>
        $(document).ready(function () {
            var container = $("#gallery");

            // Adding a lot of images
            for (var i = 0; i < 20; i++) {
                $("<img/>", {
                    'src': '/images/clear.gif',
                    'data-original': '/image/random?height=' + container.height(),
                    'height': '100%',
                    'class': 'lazy'
                }).appendTo(container);
            }

            $("img.lazy").show().lazyload({
//                threshold: 500,
                effect: 'fadeIn',
                effectspeed: 2000,
                container: container
            });
        });
    </script>
    <style>
        body {
        }

        #gallery {
            width: 100%;
            height: 100%;
            overflow: scroll;
            white-space: nowrap;
        }

        .lazy {
            display: none;
            /*border-style: dashed;*/
        }
    </style>
</head>
<body>
<div id="gallery"/>
</body>
</html>

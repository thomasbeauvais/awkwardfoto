<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>

<%
    final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<html>
<head>
    <title>JavaScript file upload</title>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.10.1/jquery.min.js"></script>
    <script>
        $(function() {
            $('#photo').change(function() {

                $( "#preview").empty();

                for (var index = 0; index < this.files.length; ++index) {
                    console.log(this.files[index].name);

                    var reader = new FileReader();

                    reader.onload = function (event) {
                        $("<img/>", {'src': event.target.result, 'height' : '200px'}).appendTo( $("#preview") );
                    };

                    reader.readAsDataURL(this.files[index]);
                }

                /*
                 var file = this.files[0];
                 name = file.name;
                 size = file.size;
                 type = file.type;

                 var reader = new FileReader();

                 reader.onload = function (e) {
                 $('#preview').attr('src', e.target.result);
                 }

                 reader.readAsDataURL(file);
                 */
            });

            function progressHandler(e){
                if(e.lengthComputable){
                    $('progress').attr({value:e.loaded,max:e.total});
                }
            }


            function successHandler( data, textStatus, jqXHR ){
                $('#results').html( data );
            }

            $(':button').click(function(){
                var formData = new FormData($('form')[0]);
                $.ajax({
//                    url: '/upload?uploadUrl=' + encodeURIComponent( '/imageUpload' ),  //server script to process data
                    url: '<%= blobstoreService.createUploadUrl("/upload") %>',  //server script to process data
                    type: 'POST',
                    xhr: function() {  // custom xhr
                        var myXhr = $.ajaxSettings.xhr();
                        if(myXhr.upload){ // check if upload property exists
                            myXhr.upload.addEventListener('progress',progressHandler, false); // for handling the progress of the upload
                        }
                        return myXhr;
                    },
                    //Ajax events
//                    beforeSend: beforeSendHandler,
                    success: successHandler,
//                    error: errorHandler,

                    // Form data
                    data: formData,

                    //Options to tell JQuery not to process data or worry about content-type
                    cache: false,
                    contentType: false,
                    processData: false
                });
            });

        });

    </script>
</head>
<body>

<form enctype="multipart/form-data">
    <fieldset>
        <legend>Upload photo</legend>
        <input type="file" name="photo" id="photo" multiple>
        <input type="button" value="Upload">
        <hr>
        <div id="preview"/>
    </fieldset>
</form>

<progress id="progress"/><br/>
<div id="results"></div><br/>

</body>
</html>

const $table = $('#table')
function loadData(){
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/games",
        headers: {
            "Access-Control-Allow-Origin": "http://localhost:8090",
            "Access-Control-Allow-Methods": "POST, GET, PUT, DELETE",
            "Access-Control-Allow-Headers": "Content-Type, Origin, Authorization"
        },
        success: function (responseData) {
            while(dataTableBody.firstChild){
                dataTableBody.removeChild(dataTableBody.firstChild);
            }

            const jsonData = JSON.parse(result);
            const data = [];
            Object.keys(responseData).forEach(key => {
                const value = responseData[key];
                data.push(value);
            });
            $table.bootstrapTable({data: data});
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}
document.addEventListener("DOMContentLoaded", loadData())

//$(document).ready(function () {
//                    $.getJSON("gfgdetails.json",
//                            function (data) {
//                        var student = '';
//
//                        // ITERATING THROUGH OBJECTS
//                        $.each(data, function (key, value) {
//
//                            //CONSTRUCTION OF ROWS HAVING
//                            // DATA FROM JSON OBJECT
//                            student += '<tr>';
//                            student += '<td>' +
//                                value.GFGUserName + '</td>';
//
//                            student += '<td>' +
//                                value.NoOfProblems + '</td>';
//
//                            student += '<td>' +
//                                value.TotalScore + '</td>';
//
//                            student += '<td>' +
//                                value.Articles + '</td>';
//
//                            student += '</tr>';
//                        });
//
//                        //INSERTING ROWS INTO TABLE
//                        $('#table').append(student);
//                    });
//                });
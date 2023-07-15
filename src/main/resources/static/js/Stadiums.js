document.addEventListener("DOMContentLoaded", getAllStadiums());

function getObjectId(tr) {
    const id = tr.getAttribute("data-uniqueid");
    return id;
}

function deleteStadium(tr) {
    deleteStadiumById(getObjectId(tr));
}

function readFormFieldsValues(id) {
    const data = {
        id: id,
        name: document.getElementById("name").value,
        location: document.getElementById("location").value,
    };
    const json = JSON.stringify(data);
    return json;
}

function setFormFieldsValues(data) {
    document.getElementById("name").setAttribute("value", data.name);
    document.getElementById("location").setAttribute("value", data.location);
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create stadium";

    const data = {
        name: "",
        location: "",
    };
    setFormFieldsValues(data);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = function() {
        const json = readFormFieldsValues();
        createStadium(json);
    }

    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit stadium";

    const id = getObjectId(tr);
    getStadiumById(id, setFormFieldsValues);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = function() {
        const json = readFormFieldsValues(id);
        updateStadiumById(id, json);
    }

    $("#form").modal("show");
}

function createStadium(json) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8090/stadiums",
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllStadiums();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getAllStadiums() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/stadiums",
        success: function (responseData) {
            for (let i = 0; i < responseData.length; i++) {
                responseData[i] = {
                    ...responseData[i],
                    actions:
                    '<button type="button" class="btn btn-primary btn-block" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block" onclick="deleteStadium(this.parentElement.parentElement)">Delete</button>',
                }
            }
            $("#dataTable").bootstrapTable({data: responseData});
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getStadiumById(id, onSuccessFunction) {
    return $.ajax({
        type: "GET",
        url: "http://localhost:8090/stadiums/" + id,
        success: function (responseData) {
            onSuccessFunction(responseData);
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function updateStadiumById(id, json) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8090/stadiums/" + id,
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllStadiums();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function deleteStadiumById(id) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8090/stadiums/" + id,
        success: function (responseData) {
            getAllStadiums();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}
document.addEventListener("DOMContentLoaded", getAllTeams());

function getObjectId(tr) {
    const id = tr.getAttribute("data-uniqueid");
    return id;
}

function deleteTeam(tr) {
    deleteTeamById(getObjectId(tr));
}

function readFormFieldsValues(id) {
    const data = {
        id: id,
        name: document.getElementById("name").value,
    };
    const json = JSON.stringify(data);
    return json;
}

function setFormFieldsValues(data) {
    document.getElementById("name").setAttribute("value", data.name);
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create team";

    const data = {
        name: "",
        goalsScored: 0,
        goalsReceived: 0,
        victories: 0,
        defeats: 0,
        draws: 0,
    };
    setFormFieldsValues(data);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = function() {
        const json = readFormFieldsValues();
        createTeam(json);
    }

    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit team";

    const id = getObjectId(tr);
    getTeamById(id, setFormFieldsValues);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = function() {
        const json = readFormFieldsValues(id);
        updateTeamById(id, json);
    }

    $("#form").modal("show");
}

function createTeam(json) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8090/teams",
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllTeams();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getAllTeams() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/teams",
        success: function (responseData) {
            for (let i = 0; i < responseData.length; i++) {
                responseData[i] = {
                    ...responseData[i],
                    actions:
                    '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
                }
            }
            $("#dataTable").bootstrapTable("destroy");
            $("#dataTable").bootstrapTable({data: responseData});
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getTeamById(id, onSuccessFunction) {
    return $.ajax({
        type: "GET",
        url: "http://localhost:8090/teams/" + id,
        success: function (responseData) {
            onSuccessFunction(responseData);
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function updateTeamById(id, json) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8090/teams/" + id,
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllTeams();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function deleteTeamById(id) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8090/teams/" + id,
        success: function (responseData) {
            getAllTeams();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}
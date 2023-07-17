document.addEventListener("DOMContentLoaded", getAllPlayers());

function getObjectId(tr) {
    const id = tr.getAttribute("data-uniqueid");
    return id;
}

function deletePlayer(tr) {
    deletePlayerById(getObjectId(tr));
}

function readFormFieldsValues(id) {
    const data = {
        id: id,
        name: document.getElementById("name").value,
        goalsScored: document.getElementById("goalsScored").value,
        role: document.getElementById("role").value,
        teamId: document.getElementById("teamId").value,
    };
    const json = JSON.stringify(data);
    return json;
}

function setFormFieldsValues(data) {
    document.getElementById("name").setAttribute("value", data.name);
    document.getElementById("goalsScored").setAttribute("value", data.goalsScored);
    document.getElementById("role").value = data.role;
    document.getElementById("teamId").value = data.teamId;
}

function setTeamSelectData(responseData) {
    const select = document.getElementById("teamId");
    select.innerHTML = '<option value="null">No team</option>';
    for (let i = 0; i < responseData.length; i++) {
        const option = document.createElement("option");
        option.value = responseData[i].id;
        option.text = responseData[i].name;
        select.appendChild(option);
    }
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create player";

    getAllTeams(setTeamSelectData);

    const data = {
        name: "",
        goalsScored: 0,
        role: "DEFENDER",
        teamId: null,
    };
    setFormFieldsValues(data);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = function() {
        const json = readFormFieldsValues();
        createPlayer(json);
    }

    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit player";

    getAllTeams(setTeamSelectData);

    const id = getObjectId(tr);
    getPlayerById(id, setFormFieldsValues);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = function() {
        const json = readFormFieldsValues(id);
        updatePlayerById(id, json);
    }

    $("#form").modal("show");
}

function createPlayer(json) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8090/players",
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllPlayers();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getAllPlayers() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/players",
        success: function (responseData) {
            const requests = [];
            for (let i = 0; i < responseData.length; i++) {
                responseData[i] = {
                    ...responseData[i],
                    actions:
                    '<button type="button" class="btn btn-primary btn-block" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block" onclick="deletePlayer(this.parentElement.parentElement)">Delete</button>',
                }

                if (responseData[i].teamId) {
                    const request = getTeamById(responseData[i].teamId, function(response) {
                        responseData[i].teamId = response.name;
                    });
                    requests.push(request);
                }
            }
            $.when.apply(this, requests).done(function() {
                $("#dataTable").bootstrapTable("destroy");
                $("#dataTable").bootstrapTable({data: responseData});
            });
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getPlayerById(id, onSuccessFunction) {
    return $.ajax({
        type: "GET",
        url: "http://localhost:8090/players/" + id,
        success: function (responseData) {
            onSuccessFunction(responseData);
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function updatePlayerById(id, json) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8090/players/" + id,
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllPlayers();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function deletePlayerById(id) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8090/players/" + id,
        success: function (responseData) {
            getAllPlayers();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getAllTeams(onSuccessFunction) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/teams",
        success: function (responseData) {
            onSuccessFunction(responseData);
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
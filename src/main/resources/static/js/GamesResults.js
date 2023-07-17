document.addEventListener("DOMContentLoaded", getAllGamesResults());

function getObjectId(tr) {
    const id = tr.getAttribute("data-uniqueid");
    return id;
}

function deleteRowData(tr) {
    deleteGameResultById(getObjectId(tr));
}

function readFormFieldsValues(id) {
    const data = {
        id: id,
        goalsTeamOne: document.getElementById("goalsTeamOne").value,
        goalsTeamTwo: document.getElementById("goalsTeamTwo").value,
    };
    const json = JSON.stringify(data);
    return json;
}

function setFormFieldsValues(data) {
    document.getElementById("goalsTeamOne").setAttribute("value", data.goalsTeamOne);
    document.getElementById("goalsTeamTwo").setAttribute("value", data.goalsTeamTwo);
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create games result";

    const data = {
        goalsTeamOne: 0,
        goalsTeamTwo: 0,
    };
    setFormFieldsValues(data);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = function() {
        const json = readFormFieldsValues();
        createGameResult(json);
    }

    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit games result";

    const id = getObjectId(tr);
    getGameResultById(id, setFormFieldsValues);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = function() {
        const json = readFormFieldsValues(id);
        updateGameResultById(id, json);
    }

    $("#form").modal("show");
}

function createGameResult(json) {
    $.ajax({
        type: "POST",
        url: "http://localhost:8090/games-results",
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllGamesResults();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getAllGamesResults() {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/games-results",
        success: function (responseData) {
            const requests = [];
            for (let i = 0; i < responseData.length; i++) {
                responseData[i] = {
                    ...responseData[i],
                    teamOneName: null,
                    teamTwoName: null,
                    actions:
                    '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
                    '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
                }
                const request = getGameTeamsNamesByGameResult(responseData[i].id, function(response) {
                    responseData[i].teamOneName = response.teamOneName;
                    responseData[i].teamTwoName = response.teamTwoName;
//                    responseData[i] = {
//                        ...responseData[i],
//                        ...response,
//                        actions:
//                        '<button type="button" class="btn btn-primary btn-block" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
//                        '<button type="button" class="btn btn-danger btn-block" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>',
//                    };
                });
                requests.push(request);
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

function getGameResultById(id, onSuccessFunction) {
    return $.ajax({
        type: "GET",
        url: "http://localhost:8090/games-results/" + id,
        success: function (responseData) {
            onSuccessFunction(responseData);
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function updateGameResultById(id, json) {
    $.ajax({
        type: "PUT",
        url: "http://localhost:8090/games-results/" + id,
        headers: {
            "Content-type": "application/json",
        },
        data: json,
        success: function (responseData) {
            getAllGamesResults();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function deleteGameResultById(id) {
    $.ajax({
        type: "DELETE",
        url: "http://localhost:8090/games-results/" + id,
        success: function (responseData) {
            getAllGamesResults();
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}

function getGameTeamsNamesByGameResult(id, onSuccessFunction) {
    $.ajax({
        type: "GET",
        url: "http://localhost:8090/games-results/game-teams-names/" + id,
        success: function (responseData) {
            onSuccessFunction(responseData);
        },
        error: function (error) {
            console.log(`Error ${error}`);
        },
    });
}
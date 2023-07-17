const baseURL = "http://localhost:8090";
const gamesURL = baseURL + "/games";
const gamesResultsURL = baseURL + "/games-results";
const stadiumsURL = baseURL + "/stadiums";
const teamsURL = baseURL + "/teams";
const headerForSendingJson = { "Content-type": "application/json", };

function httpRequest(obj) {
    return $.ajax({
        type: obj.requestType,
        url: obj.url,
        headers: obj.headers,
        data: obj.json,
        success: obj.onSuccessFunction,
        error: (error) => console.log(`Error ${error}`),
    });
}

function getAllGames(th) {
    let obj;
    if (th && th.textContent) {
        let columnName = th.textContent;
        obj = {
            field: th.id,
            sortDir: columnName.endsWith("↑")?1:columnName.endsWith("↓")?-1:0,
        };
        th.textContent = sortDir===0?th.textContent.replaceAll("↓", "")
            :sortDir===1?th.textContent + "↑"
            :sortDir===-1?th.textContent.replaceAll("↑", "↓")
            :null;
    }

    const json = JSON.stringify(obj);

    httpRequest({
        url: gamesURL,
        requestType: "GET",
        headers: headerForSendingJson,
        data: json,
        onSuccessFunction: populateDataTable,
    });
}

document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("DateAndTime").onclick = () => getAllGames(document.getElementById("DateAndTime"));
    document.getElementById("TeamOne").onclick = () => getAllGames(document.getElementById("TeamOne"));
    document.getElementById("TeamTwo").onclick = () => getAllGames(document.getElementById("TeamTwo"));
    document.getElementById("Stadium").onclick = () => getAllGames(document.getElementById("Stadium"));
    getAllGames();
});

function millisToFormattedDateAndTime(millis) {
    const date = new Date(millis)
    const formattedDateAndTime = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()} ${date.getHours()}:${date.getMinutes()}`;
    return formattedDateAndTime;
}

function convertFormattedDateToMillis(formattedDateAndTime) {
    if (!formattedDateAndTime) return null;
    const date = formattedDateAndTime.split(" ")[0].split("/");
    const year = date[0];
    const month = (Number(date[1]) - 1).toString();
    const day = date[2];
    const time = formattedDateAndTime.split(" ")[1].split(":");
    const hour = time[0];
    const minutes = time[1];
    return new Date(year, month, day, hour, minutes).getTime();;
}

function getObjectId(tr) {
    return tr.getAttribute("data-uniqueid");
}

function deleteRowData(tr) {
    httpRequest({
        url: gamesURL + "/" + getObjectId(tr),
        requestType: "DELETE",
        onSuccessFunction: getAllGames,
    });
}

function randomize(tr) {
    let gameResultId;
    let game;
    const requests = [];
    const maxNumberOfGoals = 10;
    const gameResultData = {
        goalsTeamOne: Math.floor(Math.random() * maxNumberOfGoals),
        goalsTeamTwo: Math.floor(Math.random() * maxNumberOfGoals),
    };
    let request = httpRequest({
        url: gamesResultsURL,
        requestType: "POST",
        headers: headerForSendingJson,
        json: JSON.stringify(gameResultData),
        onSuccessFunction: responseData => gameResultId = responseData.id,
    });
    requests.push(request);
    request = httpRequest({
        url: gamesURL + "/" + getObjectId(tr),
        requestType: "GET",
        onSuccessFunction: responseData => game = responseData,
    });
    requests.push(request);

    $.when.apply(this, requests).done(() => {
        game.gameResultId = gameResultId;
        httpRequest({
            url: gamesURL + "/" + getObjectId(tr),
            requestType: "PUT",
            headers: headerForSendingJson,
            json: JSON.stringify(game),
            onSuccessFunction: getAllGames,
        });
    });
}

function populateDataTable(responseData){
    const requests = [];
    for (let i = 0; i < responseData.length; i++) {
        responseData[i].dateAndTimeInMillis = millisToFormattedDateAndTime(responseData[i].dateAndTimeInMillis);
        responseData[i] = {
            ...responseData[i],
            actions:
            '<button type="button" class="btn btn-primary btn-block actionButton" onclick="openEditForm(this.parentElement.parentElement)">Edit</button>' +
            '<button type="button" class="btn btn-danger btn-block actionButton" onclick="deleteRowData(this.parentElement.parentElement)">Delete</button>' +
            (responseData[i].gameResultId ? '' :
            '<button type="button" class="btn btn-warning btn-block actionButton" onclick="randomize(this.parentElement.parentElement)">Randomize game result</button>'),
        };

        if (responseData[i].teamOneId) {
            var request = httpRequest({
                url: teamsURL + "/" + responseData[i].teamOneId,
                requestType: "GET",
                onSuccessFunction: (response) => responseData[i].teamOneId = response.name,
            });
            requests.push(request);
        }
        if (responseData[i].teamTwoId) {
            request = httpRequest({
                url: teamsURL + "/" + responseData[i].teamTwoId,
                requestType: "GET",
                onSuccessFunction: (response) => responseData[i].teamTwoId = response.name,
            });
            requests.push(request);
        }
        if (responseData[i].stadiumId) {
            request = httpRequest({
                url: stadiumsURL + "/" + responseData[i].stadiumId,
                requestType: "GET",
                onSuccessFunction: (response) => responseData[i].stadiumId = response.name,
            });
            requests.push(request);
        }
        if (responseData[i].gameResultId) {
            request = httpRequest({
                url: gamesResultsURL + "/" + responseData[i].gameResultId,
                requestType: "GET",
                onSuccessFunction: (response) => responseData[i].gameResultId = response.goalsTeamOne + ":" + response.goalsTeamTwo,
            });
            requests.push(request);
        }
    }
    $.when.apply(this, requests).done(function() {
        $("#dataTable").bootstrapTable("destroy");
        $("#dataTable").bootstrapTable({data: responseData});
    });
}

function setSelectsData() {
    function setSelectData(selectId, data, valueFunction, defaultValue) {
        const select = document.getElementById(selectId);
        select.innerHTML = `<option value>${defaultValue}</option>`;
        for (let i = 0; i < data.length; i++) {
            const option = document.createElement("option");
            option.value = data[i].id;
            option.text = valueFunction(data[i]);
            select.appendChild(option);
        }
    }

    httpRequest({
        url: teamsURL,
        requestType: "GET",
        onSuccessFunction: response => {
            setSelectData("teamOneId", response, obj => obj.name, "No team");
            setSelectData("teamTwoId", response, obj => obj.name, "No team");
       },
    });

    httpRequest({
        url: stadiumsURL,
        requestType: "GET",
        onSuccessFunction: response => setSelectData("stadiumId", response, obj => obj.name, "No stadium"),
    });

    httpRequest({
        url: gamesResultsURL,
        requestType: "GET",
        onSuccessFunction: response => setSelectData("gameResultId", response, obj => obj.goalsTeamOne + ":" + obj.goalsTeamTwo, "No game result"),
    });
}

function setFormFieldsValues(data) {
    $("#dateAndTimeInMillis").val(millisToFormattedDateAndTime(data.dateAndTimeInMillis));
        const v1=data.teamOneId.toString();
        alert(v1);
//    $(`#teamOneId option[textContent="${v1}"]`).attr('selected', true);
    $("#teamOneId").value = Number(v1);
    $("#teamTwoId").attr("value", data.teamTwoId);
    $("#stadiumId").attr("value", data.stadiumId);
    $("#gameResultId").attr("value", data.gameResultId);
}
//    $("#teamOneId").val(data.teamOneId.toString()).change();
function readFormFieldsValues(id) {
    return data = {
        id: id,
        dateAndTimeInMillis: convertFormattedDateToMillis(document.getElementById("dateAndTimeInMillis").value),
        teamOneId: document.getElementById("teamOneId").value ? document.getElementById("teamOneId").value : null,
        teamTwoId: document.getElementById("teamTwoId").value ? document.getElementById("teamTwoId").value : null,
        stadiumId: document.getElementById("stadiumId").value ? document.getElementById("stadiumId").value : null,
        gameResultId: document.getElementById("gameResultId").value ? document.getElementById("gameResultId").value : null,
    };
}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create game";

    setSelectsData();

    const data = {
        dateAndTimeInMillis: Date.now(),
        teamOneId: "",
        teamTwoId: "",
        stadiumId: "",
        gameResultId: "",
    };
    setFormFieldsValues(data);

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();
        if(!data.dateAndTimeInMillis) {
            alert("Wrong date and time value!");
            return;
        }
        httpRequest({
            url: gamesURL,
            requestType: "POST",
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllGames,
        });
        $("#form").modal("hide");
    };

    $("#form").modal("show");
}

function openEditForm(tr) {
    document.getElementById("formTitle").textContent = "Edit game";

    setSelectsData();

    const id = getObjectId(tr);

    httpRequest({
        url: gamesURL + "/" + id,
        requestType: "GET",
        onSuccessFunction: setFormFieldsValues,
    });

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = () => {
        const data = readFormFieldsValues(id);
        if(!data.dateAndTimeInMillis || data.dateAndTimeInMillis < 0) {
            alert("Wrong date and time value!");
            return;
        }
        httpRequest({
            url: gamesURL + "/" + id,
            requestType: "PUT",
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllGames,
        });
        $("#form").modal("hide");
    };

    $("#form").modal("show");
}
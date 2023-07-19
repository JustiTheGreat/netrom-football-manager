const baseURL = "http://localhost:8090";
const gamesURL = baseURL + "/games";
const gamesResultsURL = baseURL + "/games-results";
const stadiumsURL = baseURL + "/stadiums";
const teamsURL = baseURL + "/teams";
const headerForSendingJson = { "Content-type": "application/json", };

document.addEventListener("DOMContentLoaded", () => {
//    document.getElementById("DateAndTime").onclick = () => getAllGames(document.getElementById("DateAndTime"));
//    document.getElementById("TeamOne").onclick = () => getAllGames(document.getElementById("TeamOne"));
//    document.getElementById("TeamTwo").onclick = () => getAllGames(document.getElementById("TeamTwo"));
//    document.getElementById("Stadium").onclick = () => getAllGames(document.getElementById("Stadium"));
    getAllGames();
});

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

function getAllGames(th) {
//    let obj;
//    if (th && th.textContent) {
//        let columnName = th.textContent;
//        obj = {
//            field: th.id,
//            sortDir: columnName.endsWith("↑")?1:columnName.endsWith("↓")?-1:0,
//        };
//        th.textContent = sortDir===0?th.textContent.replaceAll("↓", "")
//            :sortDir===1?th.textContent + "↑"
//            :sortDir===-1?th.textContent.replaceAll("↑", "↓")
//            :null;
//    }
//
//    const json = JSON.stringify(obj);

    httpRequest({
        url: gamesURL,
        requestType: "GET",
//        headers: headerForSendingJson,
//        data: json,
        onSuccessFunction: populateDataTable,
    });
}

function randomize(tr) {
    httpRequest({
        url: gamesURL + "/random-game-result/" + getObjectId(tr),
        requestType: "PUT",
        onSuccessFunction: getAllGames,
    });
}

function millisToFormattedDateAndTime(millis) {
    const date = new Date(millis);
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
    $.when.apply(this, requests).done(() => {
        $("#dataTable").bootstrapTable("destroy");
        $("#dataTable").bootstrapTable({data: responseData});
    });
}

function toggleResultCreation(visibility) {
    if (visibility === true || visibility === false) {
        document.getElementById("gameResultId").checked = visibility;
    }
    const hidden = !document.getElementById("gameResultId").checked;
    document.getElementById("goalsTeamOneLabel").hidden = hidden;
    document.getElementById("goalsTeamOne").hidden = hidden;
    document.getElementById("goalsTeamTwoLabel").hidden = hidden;
    document.getElementById("goalsTeamTwo").hidden = hidden;
}

function setSelectsData() {
    function setSelectData(selectId, data, valueFunction, defaultValue) {
        const select = document.getElementById(selectId);
        select.innerHTML = `<option>${defaultValue}</option>`;
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
}

function readFormFieldsValues() {
    return {
        dateAndTimeInMillis: $("#dateAndTimeInMillis").val() ? convertFormattedDateToMillis($("#dateAndTimeInMillis").val()) : null,
        teamOneId: $("#teamOneId").val(),
        teamTwoId: $("#teamTwoId").val(),
        stadiumId: $("#stadiumId").val(),
        gameResultId: $("#gameResultId").prop("name"),
        goalsTeamOne: $("#goalsTeamOne").val(),
        goalsTeamTwo: $("#goalsTeamTwo").val(),
    };
}

function setFormFieldsValues(data) {
    $("#dateAndTimeInMillis").val(data.dateAndTimeInMillis ? millisToFormattedDateAndTime(data.dateAndTimeInMillis)
        : millisToFormattedDateAndTime(Date.now()));
    $("#teamOneId").val(data.teamOneId).change();
    $("#teamTwoId").val(data.teamTwoId).change();
    $("#stadiumId").val(data.stadiumId).change();
    $("#gameResultId").prop("disabled", data.gameResultId && document.getElementById("formTitle").textContent == "Edit game");
    $("#gameResultId").prop("name", data.gameResultId);
    toggleResultCreation(data.gameResultId ? true : false);
    $("#goalsTeamOne").val(data.goalsTeamOne ? data.goalsTeamOne : 0);
    $("#goalsTeamTwo").val(data.goalsTeamTwo ? data.goalsTeamTwo : 0);
}

function dataValidation(data) {
    return !data.dateAndTimeInMillis || !data.teamOneId || !data.teamTwoId || !data.stadiumId
        ? "Please complete all necessary fields!\n" : ""
       + data.teamOneId == data.teamTwoId ? "Please select different teams!\n" : ""
       + data.goalsTeamOne < 0 || data.goalsTeamTwo < 0 || !Number.isInteger(Number(data.goalsTeamOne))
            || !Number.isInteger(Number(data.goalsTeamTwo))
        ? "Please select a sound number of goals!\n" : "";

}

function openCreateForm() {
    document.getElementById("formTitle").textContent = "Create game";

    setSelectsData();

    setFormFieldsValues({});

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Create";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();

        if (dataValidation(data)) {
            alert(dataValidation(data));
            return;
        }

        const requests = [];
        let createGameResultRequest;
        if (document.getElementById("gameResultId").checked) {
            const gameResult = {
                goalsTeamOne: data.goalsTeamOne,
                goalsTeamTwo: data.goalsTeamTwo,
            };
            createGameResultRequest = httpRequest({
                url: gamesResultsURL,
                requestType: "POST",
                headers: headerForSendingJson,
                json: JSON.stringify(gameResult),
                onSuccessFunction: (responseData) => data.gameResultId = responseData.id,
            });
            requests.push(createGameResultRequest);
        }

        $.when.apply(this, requests).done(() => httpRequest({
            url: gamesURL,
            requestType: "POST",
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllGames,
        }));
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
        onSuccessFunction: (responseData) => {
            if (responseData.gameResultId) {
                httpRequest({
                    url: gamesResultsURL + "/" + responseData.gameResultId,
                    requestType: "GET",
                    onSuccessFunction: (response) => {
                        const data = {
                            ...responseData,
                            goalsTeamOne: response.goalsTeamOne,
                            goalsTeamTwo: response.goalsTeamTwo,
                        };
                        setFormFieldsValues(data);
                    }
                });
            } else setFormFieldsValues(responseData);
        }
    });

    const finishButton = document.getElementById("dialogFinishButton");
    finishButton.textContent = "Update";
    finishButton.onclick = () => {
        const data = readFormFieldsValues();
        data.id = id;

        if (dataValidation(data)) {
            alert(dataValidation(data));
            return;
        }

        const requests = [];
        let gameResultRequest;
        if (document.getElementById("gameResultId").checked) {
            const gameResult = {
                id: data.gameResultId,
                goalsTeamOne: data.goalsTeamOne,
                goalsTeamTwo: data.goalsTeamTwo,
            };
            if (data.gameResultId) {
                gameResultRequest = httpRequest({
                    url: gamesResultsURL + "/" + data.gameResultId,
                    requestType: "PUT",
                    headers: headerForSendingJson,
                    json: JSON.stringify(gameResult),
                    onSuccessFunction: (responseData) => data.gameResultId = responseData.id,
                });
            } else if (!data.gameResultId) {
                gameResultRequest = httpRequest({
                    url: gamesResultsURL + "/" + data.gameResultId,
                    requestType: "POST",
                    headers: headerForSendingJson,
                    json: JSON.stringify(gameResult),
                    onSuccessFunction: (responseData) => data.gameResultId = responseData.id,
                });
            }
            requests.push(gameResultRequest);
        }

        $.when.apply(this, requests).done(() => httpRequest({
            url: gamesURL + "/" + id,
            requestType: "PUT",
            headers: headerForSendingJson,
            json: JSON.stringify(data),
            onSuccessFunction: getAllGames,
        }));
        $("#form").modal("hide");
    };
    $("#form").modal("show");
}
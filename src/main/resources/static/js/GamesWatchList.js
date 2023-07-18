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
    httpRequest({
        url: gamesURL + "/sorted",
        requestType: "GET",
        onSuccessFunction: populateDataTable,
    });
}

document.addEventListener("DOMContentLoaded", () => getAllGames());

setInterval(getAllGames, 3000);

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

function getObjectId(tr) {
    return tr.getAttribute("data-uniqueid");
}

function randomize(tr) {
    httpRequest({
        url: gamesURL + "/random-game-result/" + getObjectId(tr),
        requestType: "PUT",
        onSuccessFunction: getAllGames,
    });
}

function populateDataTable(responseData){
    const requests = [];
    for (let i = 0; i < responseData.length; i++) {
        responseData[i].dateAndTimeInMillis = millisToFormattedDateAndTime(responseData[i].dateAndTimeInMillis);
        responseData[i] = {
            ...responseData[i],
            status: responseData[i].gameResultId ? '<div style = "color: green">FINISHED</div>'
                : '<div style = "color: red">NOT YET PLAYED</div>' ,
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
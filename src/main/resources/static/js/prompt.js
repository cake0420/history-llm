// 폼 제출 이벤트 처리
document.getElementById('questionForm').addEventListener('submit', function(event) {
    event.preventDefault(); // 기본 폼 제출 방지
    askQuestion();
});

// 질문 처리 함수
async function askQuestion() {
    let question = document.getElementById('question').value;
    let answerDiv = document.getElementById('answer');
    let button = document.querySelector("button");

    if (question.trim() === '') {
        alert('질문을 입력하십시오.');
        return;
    }


    // 버튼 비활성화 (중복 요청 방지)
    button.disabled = true;
    button.innerText = "문답을 요청 중...";
    answerDiv.innerHTML = "<p>문답을 준비 중입니다...</p>";

    try {
        let response = await fetch(window.location.origin +"/api/test", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ question: question })
        });
        console.log("서버 요청 전송: ", question);

        let data = await response.json();
        console.log("서버 응답 데이터:", data); // 응답을 콘솔에 출력

        if (data && data.answer) {
            button.innerText = "문답 청하기";
            answerDiv.innerText =   data.answer;
            button.disabled = false;

        } else {
            answerDiv.innerText = "서버 응답에 오류가 있습니다.";
            button.innerText = "문답 청하기";
            button.disabled = false;
        }
    } catch (error) {
        console.error("오류 발생:", error);
        answerDiv.innerText = "문답을 가져오는 중 오류가 발생했습니다.";
        button.innerText = "문답 청하기";
        button.disabled = false;
    }

}
const deleteReq = () => {
    console.log("삭제 요청");
    const id = [[${board.id}]];
    location.href = "/board/delete/" + id;
}

const listReq = () => {
    const page = [[${page}]];
    location.href = "/board/paging?page="+page;
}
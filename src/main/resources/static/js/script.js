
// accordion custom
$(document).ready(function () {
  $('[data-bs-toggle="collapse"]').on('click', function () {
    var target = $(this).data('bs-target');

    // 다른 모든 collapse 요소 닫기
    $('.collapse').not(target).collapse('hide');
  });

  $('.collapse').on('hide.bs.collapse show.bs.collapse', function (e) {
    var parent = $(this).parent();
    console.log(parent,'parent')
    parent.find('.qa-icon').toggleClass('active', e.type === 'show');
    parent.find('.toggle-btn').toggleClass('rotate-180', e.type === 'show');
  });

  /* 첨부파일 커스텀 */
  $("#file").on('change',function(){
    var fileName = $("#file").val();
    $(".upload-name").val(fileName);
  });

  /* 모바일 메뉴 */

  $("button.menu-trigger").click(function() {
    if($(this).hasClass("active")){
      $(this).removeClass("active");
    } else {
      $(this).addClass("active");
    }
  });

  /*  레이어 팝업 */
  function togglePopup() {
    $('.layer-popup').toggle();
    $('.layer-backdrop').toggle($('.layer-popup').is(':visible'));
  }

  $(document).ready(function() {
    $('.layer-btn').on('click', togglePopup);
    $('.layer-popup .btn-close').on('click', togglePopup);
  });
});
// 체크박스
function checkAll() {
  var allCheckbox = document.getElementById("defaultCheckAll");
  var checkboxes = document.querySelectorAll('.form-check-input:not(#defaultCheckAll)');

  checkboxes.forEach(function(checkbox) {
    checkbox.checked = allCheckbox.checked;
  });
}

function checkIndividual() {
  var allCheckbox = document.getElementById("defaultCheckAll");
  var checkboxes = document.querySelectorAll('.form-check-input:not(#defaultCheckAll)');

  var allChecked = true;
  checkboxes.forEach(function(checkbox) {
    if (!checkbox.checked) {
      allChecked = false;
    }
  });

  allCheckbox.checked = allChecked;
}
// dropdown custom
document.addEventListener('DOMContentLoaded', (event) => {
  const dropdowns = document.querySelectorAll('.dropdown');

  dropdowns.forEach(dropdown => {
    const dropdownButton = dropdown.querySelector('.dropdown-toggle');
    const dropdownItems = dropdown.querySelectorAll('.dropdown-item');

    dropdownItems.forEach(item => {
      item.addEventListener('click', (e) => {
        e.preventDefault();
        const selectedText = item.textContent;
        dropdownButton.textContent = selectedText;
      });
    });
  });
});

(function () {
  "use strict";

  var header = document.querySelector(".site-header");
  var navToggle = document.querySelector(".nav-toggle");

  if (navToggle && header) {
    navToggle.addEventListener("click", function () {
      var isOpen = header.classList.toggle("nav-open");
      navToggle.setAttribute("aria-expanded", String(isOpen));
    });

    document.querySelectorAll(".site-nav a").forEach(function (link) {
      link.addEventListener("click", function () {
        header.classList.remove("nav-open");
        navToggle.setAttribute("aria-expanded", "false");
      });
    });
  }

  var tabs = Array.prototype.slice.call(document.querySelectorAll(".tab"));
  var panels = Array.prototype.slice.call(document.querySelectorAll(".tab-panel"));

  function selectTab(tab) {
    tabs.forEach(function (t) {
      var selected = t === tab;
      t.setAttribute("aria-selected", String(selected));
      t.tabIndex = selected ? 0 : -1;
    });

    panels.forEach(function (panel) {
      panel.hidden = panel.id !== tab.getAttribute("aria-controls");
    });
  }

  tabs.forEach(function (tab, index) {
    tab.addEventListener("click", function () {
      selectTab(tab);
    });

    tab.addEventListener("keydown", function (event) {
      var newIndex = null;

      if (event.key === "ArrowRight") {
        newIndex = (index + 1) % tabs.length;
      } else if (event.key === "ArrowLeft") {
        newIndex = (index - 1 + tabs.length) % tabs.length;
      }

      if (newIndex !== null) {
        event.preventDefault();
        tabs[newIndex].focus();
        selectTab(tabs[newIndex]);
      }
    });
  });

  document.querySelectorAll(".copy-btn").forEach(function (button) {
    button.addEventListener("click", function () {
      var block = button.closest(".code-block");
      var code = block ? block.querySelector("pre code") : null;

      if (!code) {
        return;
      }

      var text = code.textContent;
      var originalLabel = button.textContent;

      navigator.clipboard.writeText(text).then(function () {
        button.textContent = "Copied!";
        setTimeout(function () {
          button.textContent = originalLabel;
        }, 1500);
      });
    });
  });

  if (window.hljs) {
    window.hljs.highlightAll();
  }
})();

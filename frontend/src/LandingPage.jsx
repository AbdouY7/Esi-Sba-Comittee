import React from "react";

const LandingPage = () => {
  const navBarElementList = [
    { title: "Home", path: "home" },
    { title: "About Us", path: "aboutus" },
    { title: "Contact Us", path: "contact" },
  ];
  return (
    <div>
      <nav>
        <img src="" alt="" />
        <div>
          {navBarElementList.map((e, i) => {
            <div key={i}>{e.title}</div>;
          })}
        </div>
        <div>
          <button>Rigester</button>
          <button>Sign in</button>
        </div>
      </nav>
    </div>
  );
};

export default LandingPage;

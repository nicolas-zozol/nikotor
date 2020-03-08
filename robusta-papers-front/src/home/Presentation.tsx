import React from 'react';

export function Presentation() {
  return (
    <section className="my-5">
      <div className="container">
        <div className="row">
          <h1>Robusta Papers</h1>
          <div className="col-md-8 mx-auto">
            <p>
              The HTML5 video element uses an mp4 video as a source. Change the
              source video to add in your own background! The header text is
              vertically centered using flex utilities that are build into
              Bootstrap 4.
            </p>
            <p>
              The overlay color can be changed by changing the{' '}
              <code>background-color</code> of the <code>.overlay</code> class
              in the CSS.
            </p>
            <p>
              Set the mobile fallback image in the CSS by changing the
              background image of the header element within the media query at
              the bottom of the CSS snippet.
            </p>
            <p className="mb-0">
              Created by{' '}
              <a href="https://startbootstrap.com">Start Bootstrap</a>
            </p>
          </div>
        </div>
      </div>
    </section>
  );
}

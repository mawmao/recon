
# TODO

in progress:
    - [ ] add validation on inputs (should validate per page before continue to next or review)
    - [ ] setup supabase connection
        - auth:
            - [x] persist login (while offline) `to review`
                - [] store login data to be able to login previously logged in users
        - db:
            - [ ] check if there is already an entry of that form with that mfid when clicking on a form
            - create mappers for each form
                - [ ] field profile
                - [ ] cultural management
        - create review screen ui

issues:
    - login sometimes go invalid after not logging in for a few days
    - keyboard options of fields in same section fix 

todo:
    - create under construction dialog to identify unfinished features
    - add image input to form 2 and up
    - image handling
    - fix options of each form

todo(ui):
    - create confirm screen ui (should get data from local/already input forms)

refactor:
    - strip events from recon vm events (the error and confirm) and let it handle the vm events only 

future:
    - handle camera permissions
    - use supabase realtime

future(ui):
    - improve transition going to camera and out
    - see password button on login field
    - show user details on settings
    - improve overall loading states for UX
    - add on press color change to buttons for UX


consider:
    - autofill on login fields
    - creating a logging class for technical logs
    - adding placeholders for each form field
    - add conditional form fields (e.g., preset npk content vs manually input)
        - add conditional options depending on a previous field's value
    - exiting early in questions screen if user pressed back twice 




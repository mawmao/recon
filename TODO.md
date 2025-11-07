
# TODO

in progress:
    - [ ] add validation on inputs (should validate per page before continue to next or review)
    - [ ] setup supabase connection
        - auth:
            - [x] user must login online
            - [x] persist login (while online) `to review`
            - [x] persist login (while offline) `to review`
                - [] store login data to be able to login previously logged in users
        - db:
            - [ ] check if there is already an entry of that form with that mfid when clicking on a form
            - create mappers for each form
                - [x] monitoring visit
                - [ ] field profile
                - [ ] cultural management
                - [-] nutrient management (`fertilizer_applications` not repeatable yet)
                - [x] production 
                - [x] damage assessment
    - handle `nutrient management` repeating questions

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
    - clean up review screen ui

clean:
    - strip events from recon vm events (the error and confirm) and let it handle the vm events only
    - clean up review screen

future:
    - handle camera permissions
    - improve transition going to camera and out
    - see password button on login field
    - show user details on settings 
    - improve overall loading states for UX
    - use supabase realtime

consider:
    - autofill on login fields
    - creating a logging class for technical logs
    - adding placeholders for each form field
    - add conditional form fields (e.g., preset npk content vs manually input) 




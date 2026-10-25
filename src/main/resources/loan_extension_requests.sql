/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  sayan
 * Created: 25 Oct 2026
 */

CREATE TYPE EXTENSION_STATUS AS ENUM ('PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN');


CREATE TABLE loan_extension_requests(
    id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    loan_id INTEGER NOT NULL REFERENCES loans(id),
    requested_due_date DATE NOT NULL,
    status EXTENSION_STATUS DEFAULT 'PENDING' NOT NULL,
    requested_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMPTZ,
    withdrawn_at TIMESTAMPTZ,
    reason TEXT,
    
    CONSTRAINT chk_status_timestamps CHECK (
    (status = 'PENDING'   AND reviewed_at IS NULL     AND withdrawn_at IS NULL) OR
    (status = 'APPROVED'  AND reviewed_at IS NOT NULL AND withdrawn_at IS NULL) OR
    (status = 'REJECTED'  AND reviewed_at IS NOT NULL AND withdrawn_at IS NULL) OR
    (status = 'WITHDRAWN' AND reviewed_at IS NULL     AND withdrawn_at IS NOT NULL)
    )
);

CREATE UNIQUE INDEX uq_loan_extension_requests_pending
ON loan_extension_requests (loan_id)
WHERE status='PENDING';

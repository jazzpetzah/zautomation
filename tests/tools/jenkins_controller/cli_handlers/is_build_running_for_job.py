#!/usr/bin/env python

import traceback

from cli_handlers.cli_handler_base import CliHandlerBase


class IsBuildRunningForJob(CliHandlerBase):
    def _build_options(self, parser):
        super(IsBuildRunningForJob, self)._build_options(parser)
        parser.add_argument('--name', required=True,
                            help='The name of Jenkins job to check. Required parameter')
        parser.add_argument('--queue_timeout',
                            help='Maximum time to wait while this job has queued items (in seconds, 300 by default). '\
                            'Set it to negative value if you want to ignore this verification.')
        parser.add_argument('--build', required=True,
                            help='Build number to check. Required parameter')
        parser.set_defaults(queue_timeout=300)

    def _invoke(self):
        parser = self._get_parser()
        args = parser.parse_args()
        job_name = self._normalize_job_name(args.name)
        job = self._jenkins.get_job(job_name)
        self._wait_while_job_in_queue(job, args.queue_timeout)
        try:
            build = job.get_build(int(args.build))
            return build.is_running()
        except Exception:
            traceback.print_exc()
            return False
